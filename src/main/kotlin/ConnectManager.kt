import base.impl.AdbExecuteImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class ConnectState(
    open val state: String
)

/**
 * 默认设备状态是设备尚未进行连接。
 */
data class DeviceNoDoConnect(
    override val state: String = "设备尚未连接"
): ConnectState(state = state)

data class DeviceOnline(
    override val state: String = "设备在线"
) : ConnectState(state = state)

data class DeviceDisConnect(
    override val state: String = "设备离线"
) : ConnectState(state = state)

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
    private fun judgeDeviceIsConnected() {
        scope.launch {
            mConnectStateFlow.emit(DeviceOnline())
        }
    }

    private fun judgeDeviceIsDisConnect() {
        scope.launch {
            mConnectStateFlow.emit(DeviceDisConnect())
        }
    }

    fun startCheckConnectJob() {
        checkConnectStateJob = scope.launch(Dispatchers.Default) {
            while(isActive) {
                if (AdbExecuteImpl.checkConnect()) {
                    judgeDeviceIsConnected()
                } else {
                    judgeDeviceIsDisConnect()
                }
                delay(2_500L)
            }
        }
    }
}