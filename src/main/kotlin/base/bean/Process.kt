package base.bean

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
    val isThirdApp: Boolean
)
