import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 *
 */
object ProcessRunnerManager {
    // adb process runner task limit is five second
    const val ProcessRunnerLimit = 5000L

    var isRunningTask: Boolean = false

    private var process: Process? = null

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _multiTaskFlow: MutableStateFlow<Map<String, List<String>>> =  MutableStateFlow(emptyMap())

    val multiTaskFlow: StateFlow<Map<String, List<String>>> get() = _multiTaskFlow

    fun startAutoEndProcess(
        command: String
    ) {
        process = Runtime.getRuntime().exec(command)
        checkIfExceedTime(command)
    }

    private fun checkIfExceedTime(
        command: String
    ) {
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
}

fun main() {
    runBlocking {
//        ProcessRunnerManager.startAutoEndProcess()
    }

}

fun InputStream.linesToFlow() = bufferedReader().lineSequence().toList()