import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 状态栏相关
 */
object StatusBar {

    //Provided to UI for state collect
    private val mStateBarFlow: MutableStateFlow<String> = MutableStateFlow("")

    val stateBarFlow: StateFlow<String> get() = mStateBarFlow

    private val mStateBarAdditionInfoFlow: MutableStateFlow<String> = MutableStateFlow("")

    //Provided to UI for state collect
    val stateBarAdditionInfoFlow: StateFlow<String> get() = mStateBarAdditionInfoFlow

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun updateStateBarState(newState: String) {
        scope.launch {
            mStateBarFlow.emit(newState)
        }
    }

    fun updateStateBarAdditionInfo(newAdditionState: String) {
        scope.launch {
            mStateBarAdditionInfoFlow.emit(newAdditionState)
        }
    }

}