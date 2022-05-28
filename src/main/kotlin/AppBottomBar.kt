import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import base.resource.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalUnitApi::class)
val appBottomBarTextUnit = TextUnit(13.0F, TextUnitType.Sp)

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun AppBottomBar(
    bottomSheetState: BottomSheetState
) {
    val connectState = ConnectManager.connectStateFlow.collectAsState()
    val shellTask = TaskManager.taskFlow.collectAsState()
    val toastInfo = ToastManager.toastFlow.collectAsState()
    val scope = rememberCoroutineScope()
    BottomAppBar(modifier = Modifier.fillMaxWidth().height(30.dp).background(shape = RoundedCornerShape(40.dp), color = Color.White), backgroundColor = BottomAppBarBgColor) {
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
            scope.launch {
                bottomSheetState.apply {
                    if (isCollapsed) expand() else collapse()
                }
            }
//            ContentPageController.navigateToPageByIndex(TaskLogPage())
//            BackMoon()
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