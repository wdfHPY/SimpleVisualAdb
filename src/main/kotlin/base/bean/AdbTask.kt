package base.bean

/**
 * 多任务执行Bean
 * @param adbShellCommandIndex shellCommand 索引
 * @param adbShellCommandStr shell的内容
 * @param adbShellResult shell执行的结果。
 */
data class AdbTask(
    val adbShellCommandIndex: Int,
    val adbShellCommandStr: String,
    var adbShellResult: List<String> = emptyList(),
    var adbShellState: ShellState = WaitForExecute()
)

data class CurrentTask(
    var adbShellCommandStr: String,
    var adbShellState: ShellState = WaitForExecute()
)


sealed class ShellState(
    open val description: String
)

data class WaitForExecute(
    override val description: String = "等待执行"
) : ShellState(description = description)

data class Running(
    override val description: String = "执行中"
) : ShellState(description = description)

data class ExecuteComplete(
    override val description: String = "执行完成"
) : ShellState(description = description)