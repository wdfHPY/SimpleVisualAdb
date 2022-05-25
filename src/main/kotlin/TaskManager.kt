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

    fun updateTaskInfo(
        newTask: ShellTask
    ) {
        scope.launch {
            mTaskFlow.emit(newTask)
        }
    }

}