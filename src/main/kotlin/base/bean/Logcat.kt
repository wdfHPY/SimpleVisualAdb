package base.bean

import org.jetbrains.annotations.TestOnly

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
)

//fun main() {
//    transfrom()
//}

fun String.transform() : Logcat ?{
    return this.split(" ").filter {
        it.isNotEmpty()
    }.let { list ->
        if (list.size >=  6) {
            Logcat(
                list[0],
                list[1],
                list[2],
                list[3],
                list[4],
                list[5],
                list.subList(6, list.size).joinToString(separator = " ")
            )
        } else {
            println("--------------------------                 $this")
            null
        }
    }
}
