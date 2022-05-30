import ProcessRunnerManager.logcatFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class LogcatOutputJobState

object LogcatNotStart : LogcatOutputJobState()

object LogcatOutputting : LogcatOutputJobState()

object LogcatPaused : LogcatOutputJobState()

/**
 * Logcat 日志管理器
 */
object LogcatManager {

    private val mLogcatJobStateFlow : MutableStateFlow<LogcatOutputJobState> = MutableStateFlow(LogcatNotStart)

    val logcatJobStateFlow: StateFlow<LogcatOutputJobState> get() = mLogcatJobStateFlow

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default) + SupervisorJob()

    private var logcatOutputJob: Job? = null

    //logcat缓存流CacheFlow。
    val logcatCacheStateFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())


    /**
     * 启动Logcat的输出。
     */
    fun startLogcatOutput() {
        initLogcatOutputJob()
        scope.launch { mLogcatJobStateFlow.emit(LogcatOutputting) }
    }

    /**
     * 暂停logcat的输出。
     */
    fun stopLogcatOutput() {
        logcatOutputJob?.cancel()
        scope.launch { mLogcatJobStateFlow.emit(LogcatPaused) }
    }

    /**
     * 初始化Logcat的输出任务。
     */
    private fun initLogcatOutputJob() {
        logcatOutputJob = scope.launch(Dispatchers.IO) {
            while(isActive) {
                delay(300L)
                logcatCacheStateFlow.emit(logcatFlow.replayCache)
            }
        }
    }

    fun clearAllCacheData() {
        scope.launch {
            stopLogcatOutput()
            logcatCacheStateFlow.emit(emptyList())
            delay(1000L)
            startLogcatOutput()
        }
    }
}