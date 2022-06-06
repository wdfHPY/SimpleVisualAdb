import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import base.resource.BottomAppBarBgColor
import base.resource.simpleAdbColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
fun main() = application {
    ConnectManager.startCheckConnectJob()
    LogcatManager.startProcessJob()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    scope.launch {
        AdbShellManager.adbPath.collect {
            AdbShellManager.updateDevicePath(it)
        }
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "",
        state = WindowState(size = DpSize(width = 1070.dp, height = 650.dp)),
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = {
                AppBottomBar(
                    scaffoldState.bottomSheetState
                )
            },
        ) {
            Content(scaffoldState)
        }
    }

}

@Composable
private fun WindowScope.AppWindowTitleBar() = WindowDraggableArea {
    Box(Modifier.fillMaxWidth().height(48.dp).background(Color.DarkGray))
}