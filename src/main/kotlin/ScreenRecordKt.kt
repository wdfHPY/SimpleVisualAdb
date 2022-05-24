import base.impl.AdbExecuteImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.TestOnly

object ScreenRecordKt {

    const val LTag = "ScreenRecordKt"

    private var screenRecordProcess: Process? = null

    private fun startScreenRecord(
        fileName: String
    ) = runCatching {
        Runtime.getRuntime().exec("adb shell screenrecord /sdcard/${fileName}.mp4")
    }

    fun startScreenRecordByUi(
        fileName: String = "emo"
    ) {
        startScreenRecord(fileName).onSuccess {
            screenRecordProcess = it
        }.onFailure {
            println("$LTag startScreenRecordByUi onFailure")
        }.onSuccess {
            println("$LTag startScreenRecordByUi onSuccess")
        }
    }

    private suspend fun stopScreenRecord() = runCatching {
        screenRecordProcess?.destroyForcibly()
    }

    suspend fun stopScreenRecordByUi() {
        stopScreenRecord().onSuccess {
            screenRecordProcess = null
            pullFileToDevice()
        }.onFailure {
            println(it.toString())
            println("$LTag stopScreenRecordByUi onFailure ${it.message} ${it.stackTrace}")
        }
    }

    private fun pullFileToDevice() {
        while (screenRecordProcess == null || screenRecordProcess?.isAlive == false) {
            println("pullFileToDevice")
            AdbExecuteImpl.pullDeviceFile(
                from = "/sdcard/emo.mp4", to = "."
            )
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