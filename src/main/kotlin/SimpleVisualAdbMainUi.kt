import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
                    TopAppBar {
                        Text("TopAppBar")
                        Text("TopAppBar2")
                        Text("TopAppBar3")
                        Text("TopAppBar4")
                        Text("TopAppBar5")
                    }
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