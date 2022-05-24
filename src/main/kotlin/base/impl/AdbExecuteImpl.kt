package base.impl

import base.AdbExecute
import base.bean.PullResultInfo
import base.convertPullResult
import base.getProcessInputStream
import java.io.BufferedReader
import java.io.InputStreamReader

class AdbExecuteImpl: AdbExecute {

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

    override fun pullDeviceFile(from: String, to: String): PullResultInfo {
        val builder = StringBuilder()
        getProcessInputStream("adb pull $from $to")?.let { ins ->
            BufferedReader(InputStreamReader(ins)).run {
                forEachLine { line -> builder.appendLine(line) }
            }
        }
        return builder.toString().convertPullResult()
    }


}