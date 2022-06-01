package base.bean

import base.bean.AdbProcess.Companion.PID_FIELD

/**
 *  Android ps 对应的进程信息。其他信息暂时不需要，等待产生需求的时候可以添加。
 *  @param pid 进程对应的 pid
 *  @param packageName 进程对应的packageName
 *  @param isThirdApp 是否是第三方的App.
 *  Android Studio中仅仅会显示非系统的App，即第三方的App。
 */
data class AdbProcess(
    val pid: String,
    val packageName: String,
    val isThirdApp: Boolean = false
) {
    companion object {
        const val PID_FIELD = 1
    }
}

fun String.transformToProcess() : AdbProcess?{
    if (startsWith("USER")) return null
    return this.split(" ").filter {
        it.isNotEmpty()
    }.let {
        if (it.size >= 2) {
            AdbProcess(
                it[PID_FIELD], it.last()
            )
        } else null
    }
}
