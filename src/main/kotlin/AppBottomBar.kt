import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import base.resource.*


@OptIn(ExperimentalUnitApi::class)
val appBottomBarTextUnit = TextUnit(11.0F, TextUnitType.Sp)

@Composable
@Preview
fun AppBottomBar() {
    val connectState = ConnectManager.connectStateFlow.collectAsState()
    val shellTask = TaskManager.taskFlow.collectAsState()
    val toastInfo = ToastManager.toastFlow.collectAsState()
    BottomAppBar(modifier = Modifier.fillMaxWidth().height(24.dp), backgroundColor = BottomAppBarBgColor) {
        /*  设备连接状态开始   */
        Icon(
            painter = when(connectState.value) {
                is DeviceNoDoConnect -> {
                    painterResource("images/connected.png")
                }
                is DeviceOnline -> {
                    painterResource("images/connected.png")
                }
                is DeviceDisConnect -> {
                    painterResource("images/disconnect.png")
                }
            },
            contentDescription = null,
            tint = when(connectState.value) {
                is DeviceNoDoConnect -> {
                    BottomAppBarNoDoConnect
                }
                is DeviceOnline -> {
                    BottomAppBarConnected
                }
                is DeviceDisConnect -> {
                    BottomAppBarDisConnect
                }
            },
            modifier = Modifier.padding(start = 3.dp)
        )
        Text(connectState.value.state, fontSize = appBottomBarTextUnit, color = BottomAppBarTextColor,
            modifier = Modifier.padding(start = 10.dp), textAlign = TextAlign.Center
        )

        /*  任务部分开始   */
        Icon(
            painter = painterResource("images/rightarrow.png"),
            contentDescription = null,
            tint = BottomAppBarRightArrow,
            modifier = Modifier.padding(start = 40.dp)
        )
        Text(shellTask.value, fontSize = appBottomBarTextUnit, color = BottomAppBarTextColor,
            modifier = Modifier.padding(start = 10.dp), textAlign = TextAlign.Center
        )
        IconButton(onClick = {
            ContentPageController.navigateToPageByIndex(TaskLogPage())
        }, modifier = Modifier.padding(start = 40.dp)) {
            Icon(
                painter = painterResource("images/tasklog.png"),
                contentDescription = null,
                tint = BottomAppBarTaskLog
            )
        }

        /*  提示部分开始   */
        Icon(
            painter = painterResource("images/rightarrow.png"),
            contentDescription = null,
            tint = BottomAppBarRightArrow,
            modifier = Modifier.padding(start = 10.dp)
        )
        Text(toastInfo.value, fontSize = appBottomBarTextUnit, color = BottomAppBarTextColor,
            modifier = Modifier.padding(start = 10.dp), textAlign = TextAlign.Center
        )
    }
}