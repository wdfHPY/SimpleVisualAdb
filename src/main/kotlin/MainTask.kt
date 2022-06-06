import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import base.getExecuteCommandProcess
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalUnitApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskPageUi() {
//    println(getCurrentPath())
    var text by remember { mutableStateOf(TextFieldValue("")) }

    val fileList by AdbShellManager.pathsFlow.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {

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
                        updateAdbShellPath(text.text)
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

        LazyColumn {
            items(fileList.size) {
                if (it != 0) Text(text = fileList[it].toString())
            }
        }
    }
}


fun updateAdbShellPath(
    executableCmd: String
) {
    if (executableCmd.startsWith("cd")) {
        //更新
        println("更新新的根目录 ${executableCmd.split(" ").last()}")
        AdbShellManager.updateAdbPathInfo(executableCmd.split(" ").last())
    }
}