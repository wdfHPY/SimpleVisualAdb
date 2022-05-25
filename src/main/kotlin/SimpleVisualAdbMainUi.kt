import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    ConnectManager.startCheckConnectJob()
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
                    AppBottomBar()
                }
            ) {
                Content()
            }
        }
    }
}