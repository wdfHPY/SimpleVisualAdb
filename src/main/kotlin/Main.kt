// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.ContentBody
import ui.StateBar
import ui.TopBar

@Composable
@Preview
fun App() {
    Window( visible = true, content = {


    }, create = { ComposeWindow() }, dispose = {},)

    var text by remember { mutableStateOf("Hello, World!") }

    val tt = ScreenRecord.ScreenRecordStateFlow.collectAsState()
    MaterialTheme {
        Scaffold(
            topBar = { TopBar()},
            bottomBar = { StateBar() }
        ) { innerPadding ->
            ContentBody()
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Simple Visual Adb") {
        MenuBar {
            Menu(text = "File") {
                Item(text = "New Project") {

                }

                Item(text = "New Project2") {

                }

                Item(text = "New Project3") {

                }

                Item(text = "New Project4") {

                }
            }
        }
        App()
    }
}
