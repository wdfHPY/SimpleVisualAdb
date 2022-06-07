import AdbShellManager.updateDisplayAdbPathInfo
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import base.bean.*
import base.resource.BottomAppBarBgColor
import base.resource.DeviceDirectory
import base.resource.DeviceFile
import mu.KotlinLogging
import java.awt.SystemColor.text


private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalUnitApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskPageUi() {

    val fileList by AdbShellManager.pathsFlow.collectAsState()

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.width(200.dp).padding(top = 30.dp).border(
                width = 1.dp,
                color = BottomAppBarBgColor
            )
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                CustomTextField(
                    modifier = Modifier.height(25.dp).width(190.dp).border(
                        width = 1.dp, color = BottomAppBarBgColor, RoundedCornerShape(2.dp)
                    ).padding(top = 0.1.dp).align(Alignment.CenterHorizontally)
                )
                Text(text = "Name", modifier = Modifier.padding(start = 3.dp, top = 3.dp))
                LazyColumn {
                    items(fileList.size) {
                        if (it != 0) {
                            FilePathItem(fileList[it])
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {

        }
    }
}

@OptIn(ExperimentalUnitApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilePathItem(
    file: DeviceFile?
) {
    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem("Open") {
                    when(file?.category) {
                        NormalFile -> {
                            updateAdbShellByTextField(AdbShellManager.adbPath.value + file.name)
                            updateDisplayAdbPathInfo(AdbShellManager.adbPath.value + file.name)
                        }

                        CurrentFile -> {

                        }

                        ParentFile -> {

                        }
                        else -> {}
                    }
                },
                ContextMenuItem("Download") {
                    ScreenRecord.startPullFileFromDevice(
                        from = AdbShellManager.adbPath.value + file?.name,
                        to = "."
                    )
                },
                ContextMenuItem("Delete") {

                },
                ContextMenuItem("Rename") {

                },
                ContextMenuItem("Permission") {

                },
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onDoubleClick = {
                        when(file?.category) {
                            NormalFile -> {
                                updateAdbShellByTextField(AdbShellManager.adbPath.value + file.name)
                                updateDisplayAdbPathInfo(AdbShellManager.adbPath.value + file.name)
                            }

                            ParentFile -> {
                                backToParentPath()
                            }
                            else -> {}
                        }
                    }
                ) {

                }
        ) {
            Icon(
                painter = painterResource(
                    if (file?.category is DirectorFile) "images/directory.png" else "images/fileicon.png"
                ),
                contentDescription = null,
                tint = if (file?.category is DirectorFile) DeviceDirectory else DeviceFile,
                modifier = Modifier.size(27.dp).padding(start = 10.dp)
            )
            Text(
                text = file?.name ?: "",
                modifier = Modifier.padding(start = 3.dp).align(Alignment.CenterVertically),
                fontSize = TextUnit(14.5f, TextUnitType.Sp)
            )
        }
    }
}


fun updateAdbShellPath(
    executableCmd: String
) {
    if (executableCmd.startsWith("cd")) {
        AdbShellManager.updateAdbPathInfo(executableCmd.split(" ").last())
    }
}

fun updateAdbShellByTextField(
    newExecuteCmd: String
) {
    if (newExecuteCmd.endsWith("/")) {
        AdbShellManager.updateAdbPathInfo(newExecuteCmd)
    } else {
        AdbShellManager.updateAdbPathInfo("$newExecuteCmd/")
    }
}

fun backToParentPath() {
    val array = AdbShellManager.adbDisplayPath.value.split("/").filter { it.isNotEmpty() }
    if (array.isEmpty()) return
    updateAdbShellByTextField("/${array.subList(0, array.size -1).joinToString("/")}")
    updateDisplayAdbPathInfo("/${array.subList(0, array.size -1).joinToString("/")}")
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Path",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    val text by AdbShellManager.adbDisplayPath.collectAsState()
    BasicTextField(modifier = modifier
        .background(
            MaterialTheme.colors.surface,
            MaterialTheme.shapes.small,
        )
        .fillMaxWidth()
        .onPreviewKeyEvent {
            if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                updateAdbShellByTextField(text)
                true
            } else {
                false
            }
        },
        value = text,
        onValueChange = {
            updateDisplayAdbPathInfo(it)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f).padding(start = 5.dp)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}
