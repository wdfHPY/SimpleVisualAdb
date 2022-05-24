// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {

        Column {

            Button(onClick = {
                println("点击开始")
//                ScreenRecordManager.startScreenRecord()
            }) {
                Text("开始录像")
            }

            Button(onClick = {
                println("点击结束")
//                ScreenRecordManager.stopScreenRecord()
            }) {
                Text("结束录像")
            }

            Button(onClick = {
                println("拉文件")
//                ScreenRecordManager.pullFileToDevice()
            }) {
                Text("导出录像")
            }

        }

    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
