import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class ConnectState

/**
 * 默认设备状态是设备尚未进行连接。
 */
data class DeviceNoDoConnect(
    val string: String = "设备尚未连接"
): ConnectState()

data class DeviceOnline(
    val string: String = "设备在线"
) : ConnectState()

data class DeviceDisConnect(
    val state: String = "设备离线"
) : ConnectState()

/**
 * 连接状态的管理器，控制连接状态的状态机。
 *
 */
object ConnectManager {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mConnectStateFlow: MutableStateFlow<ConnectState> = MutableStateFlow(DeviceNoDoConnect())

    val connectStateFlow: StateFlow<ConnectState> get() = mConnectStateFlow

    private var checkConnectStateJob: Job? = null

    /**
     * 状态机从尚未连接 -> 连接成功。
     */
    fun doConnect() {
        scope.launch {
            mConnectStateFlow.emit(DeviceOnline())
        }
    }

    fun disConnect() {
        scope.launch {
            mConnectStateFlow.emit(DeviceDisConnect())
        }
    }

    fun startCheckConnectJob() {
        if (checkConnectStateJob?.isActive == true) {
            checkConnectStateJob = scope.launch(Dispatchers.Default) {
                delay(5000L)
            }
        }
    }
}