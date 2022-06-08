import ProcessRunnerManager.logcatFlow
import base.bean.AdbProcess
import base.bean.Logcat
import base.bean.transform
import base.impl.AdbExecuteImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Logcat日志输出任务状态。
 */
sealed class LogcatOutputJobState

//Logcat 输出尚未开始
object LogcatNotStart : LogcatOutputJobState()

//Logcat 输出中
object LogcatOutputting : LogcatOutputJobState()

//Logcat 输出暂停
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
    val logcatCacheStateFlow: MutableStateFlow<List<Logcat?>> = MutableStateFlow(emptyList())

    val logcatMaxCounterStateFlow: MutableStateFlow<Int> = MutableStateFlow(30)

    var processJob: Job? = null

    //系统ProcessFlow
    private val mProcessFlow: MutableStateFlow<List<AdbProcess>> = MutableStateFlow(emptyList())

    val processFlow: StateFlow<List<AdbProcess>> get() = mProcessFlow

    var zygotePid: Int? = null

    /**
     *  Logcat 中展示的Process都是 Zygote 子进程
     */
    private fun getZygotePid() {
        zygotePid = Runtime.getRuntime().exec(" adb shell \"ps -P 1 | grep zygote\"").inputStream?.linesToFlow()?.let {
            if (it.isNotEmpty()) {
                it.first().split(" ").filter { processInfo ->
                    processInfo.isNotEmpty()
                }[1].toIntOrNull()
            } else null
        }
    }

    fun startProcessJob() {
        if (zygotePid == null) {
            getZygotePid()
        }
        processJob = scope.launch {
            zygotePid?.let { parentPid ->
                while (isActive) {
                    mProcessFlow.emit(AdbExecuteImpl.getProcessList(parentPid).filterNotNull())
                    delay(2500L)
                }
            }
        }
    }

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
                logcatCacheStateFlow.emit(logcatFlow.replayCache.asSequence().map {
                    it.transform()
                }.toList())
            }
        }
    }

    fun clearAllCacheData() {
        scope.launch {
            stopLogcatOutput()
            logcatCacheStateFlow.emit(emptyList())
            delay(1000L)
            startLogcatOutput()
            logcatMaxCounterStateFlow.emit(30)
        }
    }
}