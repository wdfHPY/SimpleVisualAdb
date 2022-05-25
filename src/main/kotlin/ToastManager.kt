import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias Toast = String

object ToastManager {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mToastFlow: MutableStateFlow<Toast> = MutableStateFlow("暂无提示信息")

    val toastFlow: StateFlow<Toast> get() = mToastFlow

    fun updateToastInfo(
        newToast: Toast
    ) {
        scope.launch {
            mToastFlow.emit(newToast)
        }
    }

}