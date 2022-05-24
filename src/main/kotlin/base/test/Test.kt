import base.impl.AdbExecuteImpl
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


fun main() {
    val adbExecuteImpl = AdbExecuteImpl()
//    println(adbExecuteImpl.availableEquipmentList())
//    println(adbExecuteImpl.checkConnect())
    println(adbExecuteImpl.pullDeviceFile("/sdcard/ice_android_images_2.05.23.native.userdebug.icego_20220523.0000.00_12.0_1f8dde6b3e.tgz", "."))
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