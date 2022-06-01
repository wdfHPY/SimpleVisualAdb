package base.bean

import LogcatManager
import base.bean.Logcat.Companion.COMPLETE_SIZE
import base.bean.Logcat.Companion.DATE_FIELD
import base.bean.Logcat.Companion.MESSAGE_FIELD
import base.bean.Logcat.Companion.PID_FIELD
import base.bean.Logcat.Companion.PRIORITY_FIELD
import base.bean.Logcat.Companion.TAG_FIELD
import base.bean.Logcat.Companion.TID_FIELD
import base.bean.Logcat.Companion.TIME_FIELD

/**
 * logcat 的日志格式。
 * date time PID-TID/package priority/tag: message
 */
data class Logcat(
    val date: String,
    val time: String,
    val pid: String,
    val tid: String,
    val priority: String,
    val tag: String,
    val message: String,
    val packageName: String = "",
) {
    companion object {
        const val DATE_FIELD = 0
        const val TIME_FIELD = 1
        const val PID_FIELD = 2
        const val TID_FIELD = 3
        const val PRIORITY_FIELD = 4
        const val TAG_FIELD = 5
        const val MESSAGE_FIELD = 6
        const val COMPLETE_SIZE = 6
    }
}

fun String.transform(): Logcat? {
    return this.split(" ").filter {
        it.isNotEmpty()
    }.let { list ->
        if (list.size >= COMPLETE_SIZE) {
            //只用成功解析成需要的字符串时才会这么封装成一个对象
            Logcat(
                date = list[DATE_FIELD],
                time = list[TIME_FIELD],
                pid = list[PID_FIELD],
                tid = list[TID_FIELD],
                priority = list[PRIORITY_FIELD],
                tag = list[TAG_FIELD],
                message = list.subList(MESSAGE_FIELD, list.size).joinToString(separator = " "),
                packageName = LogcatManager.processFlow.value.find {
                    it.pid == list[PID_FIELD]
                }.let {
                    it?.packageName ?: "?"
                }
            )
        } else {
            null
        }
    }
}
