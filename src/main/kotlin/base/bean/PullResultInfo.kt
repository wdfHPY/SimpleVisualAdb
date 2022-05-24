package base.bean

/**
 * @param deviceFilePath 拉取文件路径信息。
 * @param targetFilePath 目标文件路径信息。
 * @param pullResult 拉去文件动作结果。
 * @param pullActionInfo 拉取文件结果。
 */
data class PullResultInfo(
    val deviceFilePath: String = "",
    val targetFilePath: String = "",
    val pullResult: Boolean = false,
    val pullActionInfo: PullActionInfo? = null
)

data class PullActionInfo(
    val pullFileNumbers: Int,
    val skipFileNumbers: Int,
    val pullSize: Long,
    val pullSpeed: Double,
    val totalTime: Double
)