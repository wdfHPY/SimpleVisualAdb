import base.impl.AdbExecuteImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jetbrains.annotations.TestOnly

object ScreenRecordKt {

    private val logger = KotlinLogging.logger {}

    private var screenRecordProcess: Process? = null

    private var ifScreenRecording: Boolean = false

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

    fun stopScreenRecordByUi() {
        stopScreenRecord().onSuccess {
            logger.info { "stopScreenRecord onSuccess" }
            screenRecordProcess = null
            ifScreenRecording = false
            pullFileToDevice()
        }.onFailure { throwable ->
            logger.error(throwable) { "stopScreenRecord onFailure" }
        }
    }

    private fun pullFileToDevice() {
        while (screenRecordProcess == null || screenRecordProcess?.isAlive == false) {
            logger.info { "start pullFileToDevice" }
            AdbExecuteImpl.pullDeviceFile(from = "/sdcard/emo.mp4", to = ".")
        }
    }

}

@TestOnly
fun main() {
//    val o = ScreenRecordKt()
    runBlocking {
        ScreenRecordKt.startScreenRecordByUi()
        delay(20 * 1000L)
        ScreenRecordKt.stopScreenRecordByUi()
//        ScreenRecordKt.pullFileToDevice()
    }
}