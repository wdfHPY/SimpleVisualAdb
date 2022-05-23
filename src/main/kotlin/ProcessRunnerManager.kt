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

    var process: Process? = null

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val multiTaskFlow: Flow<Map<String, List<String>>> = emptyFlow()

    fun startAutoEndProcess() {
        process = Runtime.getRuntime().exec("adb shell ls /sdcard/")
        checkIfExceedTime()
    }

    private fun checkIfExceedTime() {
        runBlocking {
            try {
                isRunningTask = true
                withTimeout(ProcessRunnerLimit) {
                    while(process?.isAlive == true) {
                        process?.inputStream?.linesToFlow().let {

                        }
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
        ProcessRunnerManager.startAutoEndProcess()
    }

}

fun InputStream.linesToFlow() = bufferedReader().lineSequence().asFlow().flowOn(Dispatchers.IO)