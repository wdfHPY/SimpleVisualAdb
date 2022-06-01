package base.impl

import base.AdbExecute
import base.bean.AdbProcess
import base.bean.transformToProcess
import base.getProcessInputStream
import linesToFlow
import java.io.BufferedReader
import java.io.InputStreamReader


object AdbExecuteImpl: AdbExecute {

    override fun availableEquipmentList(): List<String> {
        val list = mutableListOf<String>()
        getProcessInputStream("adb devices")?.let { ins ->
            BufferedReader(InputStreamReader(ins)).run {
                forEachLine { line ->
                    if (line != "List of devices attached") {
                        list.add(line)
                    }
                }
            }
        }
        return list.toList()
    }

    override fun checkConnect(): Boolean {
        var state = true
        getProcessInputStream("adb get-state")?.let { ins ->
            BufferedReader(InputStreamReader(ins)).run {
                if (readLine() != "device") {
                    state = false
                }
            }
        }
        return state
    }

    override fun getProcessList(
        parentPid: Int
    ): List<AdbProcess?> {
        return Runtime.getRuntime().exec("adb shell \"ps -P $parentPid\"").inputStream?.linesToFlow()?.map {
            it.transformToProcess()
        } ?: emptyList()
    }

//    override fun pullDeviceFile(from: String, to: String): PullResultInfo {
//        val builder = StringBuilder()
//        getProcessInputStream("adb pull $from $to")?.let { ins ->
//            BufferedReader(InputStreamReader(ins)).run {
//                forEachLine { line -> builder.appendLine(line) }
//            }
//        }
//        return builder.toString().convertPullResult()
//    }


}