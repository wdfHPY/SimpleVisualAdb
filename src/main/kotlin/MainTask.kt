import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import base.bean.AdbPath
import base.bean.DeviceFile
import base.getExecuteCommandProcess
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalUnitApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskPageUi() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Box(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = {
                Text(
                    "Enter your multitasking, a single line represents one task.",
                    fontSize = TextUnit(18.0f, TextUnitType.Sp)
                )
            },
            label = {
                Text("Multitasking", fontSize = TextUnit(18.0f, TextUnitType.Sp))
            },
            modifier = Modifier.height(56.dp).width(550.dp).padding(start = 25.dp)
                .border(width = 1.dp, color = Color(0xffd1d1d1), shape = RoundedCornerShape(10.dp)).onPreviewKeyEvent {
                    if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                        println("拦截回车")
                        getOutputStream(text.text)
                        getInputStream()
                        true
                    } else {
                        false
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = Color(0xff459bac)
            ),
            maxLines = Int.MAX_VALUE,
            singleLine = false,
            textStyle = TextStyle(fontSize = TextUnit(18.0f, TextUnitType.Sp))
        )
    }
}

private var process: Process? = null

fun getOutputStream(
    cmd: String
) {
    process = getExecuteCommandProcess("adb shell $cmd").getOrNull()
    process?.outputStream?.write(cmd.toByteArray())
}

fun getInputStream() {
    process?.inputStream?.bufferedReader()?.lines()?.forEach {
        println(it)
    }
}

/**
 * 获取设备的文件列表。
 * @return 设备的文件列表
 */
fun getCurrentPath(): AdbPath {
    TODO()
}