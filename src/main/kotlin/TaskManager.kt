import base.bean.AdbTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias ShellTask  = String

object TaskManager {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mTaskFlow: MutableStateFlow<ShellTask> = MutableStateFlow("暂无任务")

    val taskFlow: StateFlow<ShellTask> get() = mTaskFlow

    /**
     * 更新任务信息。
     */
    fun updateTaskInfo(
        newTask: ShellTask
    ) {
        scope.launch {
            mTaskFlow.emit(newTask)
        }
    }


    /**
     * 将字符串转换为任务列表.
     */
    fun String.taskTransform(): List<AdbTask> {
        return mutableListOf<AdbTask>().also { result ->
            split("\n").forEachIndexed { index, s ->
                if (s.isNotEmpty()) {
                    result.add(AdbTask(adbShellCommandIndex = index, adbShellCommandStr = s))
                }
            }
        }.toList()
    }

}