import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalMaterialApi::class)
fun main() = application {
    ConnectManager.startCheckConnectJob()
    val scaffoldState = rememberBottomSheetScaffoldState()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Simple Visual Adb",
    ) {
        val count = remember { mutableStateOf(0) }
        MaterialTheme {
            Scaffold(
                topBar = {
                    AppTopBar()
                },
                bottomBar = {
                    AppBottomBar(
                        scaffoldState.bottomSheetState
                    )
                }
            ) {
                Content(scaffoldState)
            }
        }
    }
}