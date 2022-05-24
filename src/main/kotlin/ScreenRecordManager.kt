import base.getExecuteCommandProcess
import base.getExecuteCommandProcessNoBlock
import base.impl.AdbExecuteImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ScreenRecordManager {

    private var recordProcess: Process? = null

    var isRecording: Boolean = false

    fun startScreenRecord() {
        println("startScreenRecord")
        getExecuteCommandProcessNoBlock("adb shell screenrecord /sdcard/xxx.mp4").onSuccess {
            println("startScreenRecord success")
            recordProcess = it
        }.onFailure {
            println("startScreenRecord Fail")
        }
    }

    fun stopScreenRecord() {
        println("执行stopScreenRecord")
        if (recordProcess?.isAlive == true) {
            println("即将结束录像")
            recordProcess?.destroy()
        }
    }

    fun pullFileToDevice() {
        println("pullFileToDevice")
//        AdbExecuteImpl.pullDeviceFile(
//            from = "/sdcard/xxx.mp4", to = "."
//        )
    }

}