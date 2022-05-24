import base.impl.AdbExecuteImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


fun main() {
    runBlocking {
        ScreenRecordManager.startScreenRecord()
        delay(20000L)
        println(ScreenRecordManager.stopScreenRecord())
        delay(2000L)
        println(ScreenRecordManager.pullFileToDevice())
    }
//    val adbExecuteImpl = AdbExecuteImpl

//    repairByteCode()
}

fun repairByteCode() {
    File("sss.png").inputStream().readBytes().let {
        repair(it)?.let { it1 -> Files.write(Paths.get("ttt.png"), it1) }
    }
}

fun repair(encoded: ByteArray): ByteArray? {
    val baos = ByteArrayOutputStream()
    var i = 0
    try {
        while (i < encoded.size) {
            if (encoded.size > i + 1 && encoded[i].toInt() == 0x0d && encoded[i + 1].toInt() == 0x0a) {
                baos.write(0x0a)
                i++
            } else {
                baos.write(encoded[i].toInt())
            }
            i++
        }
        baos.close()
    } catch (ioe: IOException) {
    }
    return baos.toByteArray()
}