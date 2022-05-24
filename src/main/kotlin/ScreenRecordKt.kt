import base.impl.AdbExecuteImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jetbrains.annotations.TestOnly

/**
 * 现在出现一个问题，如果是在黑屏的情况下，录屏功能使用不了。
 * 后面可能在录屏开始之前需要点亮屏幕。
 */
object ScreenRecord {

    private val logger = KotlinLogging.logger {}

    private var screenRecordProcess: Process? = null

    private var ifScreenRecording: Boolean = false

    //这里需要作为Ui的状态来显示出来。
    val isScreenRecording get() = ifScreenRecording

    private fun startScreenRecord(
        fileName: String
    ) = runCatching {
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
            logger.error(throwable) { "startScreenRecord onFailure" }
        }.onSuccess {
            logger.info {
                "startScreenRecord onSuccess"
            }
        }
    }

    suspend fun stopScreenRecordByUi() {
        stopScreenRecord().onSuccess {
            logger.info { "stopScreenRecord onSuccess" }
            screenRecordProcess = null
            ifScreenRecording = false
            delay(500L)
            pullFileToDevice()
        }.onFailure { throwable ->
            logger.error(throwable) { "stopScreenRecord onFailure" }
        }
    }

    private fun pullFileToDevice() {
        if (screenRecordProcess == null || screenRecordProcess?.isAlive == false) {
            logger.info { "start pullFileToDevice" }
            AdbExecuteImpl.pullDeviceFile(from = "/sdcard/emo.mp4", to = ".")
        }
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