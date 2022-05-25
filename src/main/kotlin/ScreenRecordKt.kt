import base.convertPullResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jetbrains.annotations.TestOnly
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 现在出现一个问题，如果是在黑屏的情况下，录屏功能使用不了。
 * 后面可能在录屏开始之前需要点亮屏幕。
 */
object ScreenRecord {

    private val logger = KotlinLogging.logger {}

    private var screenRecordProcess: Process? = null

    private var pullFileProcess: Process? = null

    private var ifScreenRecording: Boolean = false

    private var ifPullingScreenFile: Boolean = false

    //这里需要作为Ui的状态来显示出来。
    val isScreenRecording get() = ifScreenRecording

    private fun startScreenRecord(
        fileName: String
    ) = runCatching {
        TaskManager.updateTaskInfo("adb shell screenrecord /sdcard/${fileName}.mp4")
        Runtime.getRuntime().exec("adb shell screenrecord /sdcard/${fileName}.mp4")
    }

    private fun stopScreenRecord() = runCatching {
        screenRecordProcess?.destroyForcibly()
    }

    /**
     * 屏幕点击开始录像。
     * @param fileName 等待保存的文件名称。
     */
    fun startScreenRecordByUi(
        fileName: String = "emo"
    ) {
        startScreenRecord(fileName).onSuccess {
            screenRecordProcess = it
            ifScreenRecording = true
        }.onFailure { throwable ->
            ToastManager.updateToastInfo("启动屏幕失败")
            logger.error(throwable) { "startScreenRecord onFailure" }
        }.onSuccess {
            ToastManager.updateToastInfo("开启屏幕录屏，正在录屏中...")
            logger.info { "startScreenRecord onSuccess" }
        }
    }

    suspend fun stopScreenRecordByUi() {
        stopScreenRecord().onSuccess {
            logger.info { "stopScreenRecord onSuccess" }
            screenRecordProcess = null
            ifScreenRecording = false
            ToastManager.updateToastInfo("停止录屏")
            delay(500L)
            pullFileToDevice()
        }.onFailure { throwable ->
            ToastManager.updateToastInfo("停止屏幕录屏失败")
            logger.error(throwable) { "stopScreenRecord onFailure" }
        }
    }

    private fun pullFileToDevice() {
        if (screenRecordProcess == null || screenRecordProcess?.isAlive == false) {
            logger.info { "start pullFileToDevice" }
            startPullFileFromDevice(from = "/sdcard/emo.mp4", to = ".")
        }
    }

    private fun startPullFileFromDevice(from: String , to: String) {
        val builder = StringBuilder()
        pullFileProcess = kotlin.runCatching {
            TaskManager.updateTaskInfo("adb pull $from $to")
            Runtime.getRuntime().exec(
                "adb pull $from $to"
            ).also {
                it.waitFor()
            }
        }.getOrNull()

        pullFileProcess?.inputStream?.let { ins ->
            ifPullingScreenFile = true
            ToastManager.updateToastInfo("开始拉取文件")
            BufferedReader(InputStreamReader(ins)).run {
                forEachLine { line -> builder.appendLine(line) }
            }
        }

        builder.toString().convertPullResult()
        pullFileProcess?.destroy()
        ToastManager.updateToastInfo("文件拉取完成")
        logger.info { "文件拉取完成" }
        ifPullingScreenFile = false
    }



}

@TestOnly
fun main() {
//    val o = ScreenRecordKt()
    runBlocking {
        ScreenRecord.startScreenRecordByUi()
        delay(20 * 1000L)
        ScreenRecord.stopScreenRecordByUi()
//        ScreenRecordKt.pullFileToDevice()
    }
}