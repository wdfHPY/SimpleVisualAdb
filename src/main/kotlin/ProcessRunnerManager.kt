import base.bean.CurrentTask
import base.bean.ExecuteComplete
import base.bean.ShellState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object ProcessRunnerManager {
    // adb process runner task limit is five second
    private const val ProcessRunnerLimit = 5000L

    var isRunningTask: Boolean = false

    private var process: Process? = null

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _multiTaskFlow: MutableStateFlow<MutableMap<String, List<String>>> =  MutableStateFlow(mutableMapOf())

    val multiTaskFlow: StateFlow<MutableMap<String, List<String>>> get() = _multiTaskFlow

    private val _multiTaskStateFlow: MutableStateFlow<MutableMap<String, ShellState>> =  MutableStateFlow(mutableMapOf())

    val multiTaskStateFlow: StateFlow<MutableMap<String, ShellState>> get() = _multiTaskStateFlow

    var logcatFlow: MutableSharedFlow<String> = MutableSharedFlow(
        replay = 2000,
        extraBufferCapacity = 1000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val logcatCacheStateFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    fun startAutoEndProcess(
        command: String
    ) {
        process = Runtime.getRuntime().exec("adb shell $command")
        process?.inputStream?.let { stream ->
            scope.launch {
                val newTask = _multiTaskFlow.value.toMutableMap()
                val newTaskState = _multiTaskStateFlow.value.toMutableMap()
                newTask[command] = stream.linesToFlow()
                newTaskState[command] = ExecuteComplete()
                _multiTaskFlow.emit(newTask)
                _multiTaskStateFlow.emit(newTaskState)
            }
        }
        checkIfExceedTime()
    }

    private fun checkIfExceedTime() {
        runBlocking {
            try {
                isRunningTask = true
                withTimeout(ProcessRunnerLimit) {
                    while(process?.isAlive == true) {
                        delay(500L)
                    }
                    println("5s 内运行结束")
                    process = null
                    isRunningTask = false
                }
            } catch (e: Exception) {
                process?.destroy()
                process = null
                isRunningTask = true
                println("现在超时。")
            }
        }
    }

    fun startLogcat() {
        scope.launch {
            scope.launch(Dispatchers.IO) {
                process = Runtime.getRuntime().exec("adb logcat -b all")
                process?.inputStream?.let { ins ->
                    BufferedReader(InputStreamReader(ins)).run {
                        forEachLine { line ->
                            scope.launch {
                                if (line.length > LogcatManager.logcatMaxCounterStateFlow.value) {
                                    LogcatManager.logcatMaxCounterStateFlow.emit(line.length)
                                }
                                logcatFlow.emit(line)
                            }
                        }
                    }
                }
            }

//            scope.launch(Dispatchers.IO) {
//                while(isActive) {
//                    delay(300L)
//                    logcatCacheStateFlow.emit(logcatFlow.replayCache)
//                }
//            }
            LogcatManager.startLogcatOutput()
        }
    }
}

fun main() {
    runBlocking {
        ProcessRunnerManager.startAutoEndProcess("adb shell screenrecord /sdcard/demo.mp4")
    }

}

fun InputStream.linesToFlow() = bufferedReader().lineSequence().toList()

fun InputStream.linesAsFlow() = bufferedReader().lineSequence().toList().asFlow()