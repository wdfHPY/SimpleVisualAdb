import TaskManager.taskTransform
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.AwtWindow
import base.bean.AdbTask
import base.bean.CurrentTask
import base.bean.filterByPriority
import base.resource.BottomAppBarTaskLog
import base.resource.BottomAppBarTextColor
import base.resource.TaskLogBarBg
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import mu.KotlinLogging
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.text.SimpleDateFormat

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
    bottomSheetState: BottomSheetScaffoldState
) {
    val state = ContentPageController.pageFlow.collectAsState()
    Row {
        Column(modifier = Modifier.fillMaxHeight().width(175.dp).background(Color(0xfff4f4f4))) {
            TimeArea()

            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xffd1d1d1)))

            Task(state.value)

            MultiTask(state.value)

            TaskLogger(state.value)

            Logcat(state.value)

            Setting(state.value)

            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xffd1d1d1)))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            when (state.value) {
                is TaskPage -> TaskPageUi()
                is MultitaskPage -> MultitaskPageUi()
                is TaskLoggerPage -> TaskLoggerPageUi()
                is LogcatPage -> LogcatUi()
                is SettingPage -> TaskLogPageUi()
            }
            Box(modifier = Modifier.height(30.dp).fillMaxWidth().background(Color.Red))
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TimeArea() {
    Column(modifier = Modifier.fillMaxWidth().height(80.dp).background(Color(0xfff4f4f4))) {
        Box(modifier = Modifier.height(40.dp).fillMaxWidth().align(Alignment.CenterHorizontally)) {
            Text(
                "22:42",
                modifier = Modifier.align(Alignment.Center),
                fontSize = TextUnit(28.0F, TextUnitType.Sp),
                fontWeight = FontWeight.W100,
                fontFamily = FontFamily.Monospace
            )
        }

        Box(modifier = Modifier.height(40.dp).fillMaxWidth().align(Alignment.CenterHorizontally)) {
            Text(
                text = "2022/05/28",
                modifier = Modifier.align(Alignment.Center),
                fontSize = TextUnit(20.0F, TextUnitType.Sp),
                fontWeight = FontWeight.W100,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Task(
    value: UiContentPage
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
            ContentPageController.navigateToPageByIndex(TaskPage())
        }.background(
            if (value is TaskPage) Color.White else Color(0xfff4f4f4)
        )
    ) {
        Text(
            "Task",
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(16.0F, TextUnitType.Sp),
            fontWeight = FontWeight.W100,
            fontFamily = FontFamily.Monospace
        )
    }
}



@OptIn(ExperimentalUnitApi::class)
@Composable
fun MultiTask(value: UiContentPage) {
    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
            ContentPageController.navigateToPageByIndex(MultitaskPage())
        }.background(
            if (value is MultitaskPage) Color.White else Color(0xfff4f4f4)
        )
    ) {
        Text(
            "Multitasking",
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(16.0F, TextUnitType.Sp),
            fontWeight = FontWeight.W100,
            fontFamily = FontFamily.Monospace
        )
    }
}

@OptIn(ExperimentalUnitApi::class, ExperimentalFoundationApi::class)
@Composable
fun MultitaskPageUi() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var list by remember { mutableStateOf(emptyList<AdbTask>()) }
    val scope = rememberCoroutineScope()

    val multiTaskTask = ProcessRunnerManager.multiTaskFlow.collectAsState()
    val multiTaskStateTask = ProcessRunnerManager.multiTaskStateFlow.collectAsState()
    val isFileChooserOpen = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().absolutePadding(bottom = 30.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(80.dp)) {
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
                modifier = Modifier.height(56.dp).align(Alignment.CenterVertically).width(550.dp).padding(start = 25.dp)
                    .border(width = 1.dp, color = Color(0xffd1d1d1), shape = RoundedCornerShape(10.dp)),
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

            IconButton(onClick = {
                list = text.text.taskTransform()
            }, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 10.dp)) {
                Icon(
                    painter = painterResource("images/transform.png"),
                    contentDescription = "转换",
                    tint = Color(0xffd1d1d1)
                )
            }

            IconButton(onClick = {
                scope.launch(Dispatchers.Default) {
                    runMultiTask(list)
                }
            }, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 10.dp)) {
                Icon(
                    painter = painterResource("images/run.png"),
                    contentDescription = null,
                    tint = Color(0xffd1d1d1)
                )
            }

            IconButton(onClick = {
                isFileChooserOpen.value = true
            }, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 10.dp)) {
                Icon(
                    painter = painterResource("images/export.png"),
                    contentDescription = null,
                    tint = Color(0xffd1d1d1)
                )
            }

            if (isFileChooserOpen.value) {
                FileDialog(
                    onCloseRequest = {
                        isFileChooserOpen.value = false
                        scope.launch(Dispatchers.Default) {
                            exportAsFile(it)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xffd1d1d1)))

        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(list.size) { index ->
                    Card(
                        backgroundColor = Color(0xfff4f4f4),
                        modifier = Modifier.padding(4.dp).fillMaxWidth().height(160.dp),
                        elevation = 8.dp,
                    ) {
                        Column {
                            Text(
                                text = "Task - ${index + 1}",
                                fontSize = 18.sp,
                                color = Color(0xff459bac),
                                modifier = Modifier.fillMaxWidth().height(30.dp).padding(top = 8.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W400
                            )

                            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xffd1d1d1)))

                            Text(
                                text = list[index].adbShellCommandStr,
                                fontSize = 14.sp,
                                color = Color(0xff333333),
                                modifier = Modifier.fillMaxWidth().height(45.dp).padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xffd1d1d1)))
                            Text(
                                text = "Result: ${
                                    if (multiTaskTask.value[list[index].adbShellCommandStr].isNullOrEmpty()) {
                                        "暂无返回值"
                                    } else {
                                        multiTaskTask.value[list[index].adbShellCommandStr].toString()
                                    }
                                }",
                                color = Color(0xff010101),
                                modifier = Modifier.fillMaxWidth().height(55.dp).padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xffd1d1d1)))
                            Row(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "state: ${
                                        multiTaskStateTask.value[list[index].adbShellCommandStr]?.description ?: "等待执行"
                                    }",
                                    modifier = Modifier.padding(start = 5.dp).align(Alignment.CenterVertically),
                                    color =
                                    Color(0xff0e932e),
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "保存运行结果", SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(directory + file)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

fun runMultiTask(list: List<AdbTask>) {
    list.onEach {
        ProcessRunnerManager.startAutoEndProcess(it.adbShellCommandStr)
    }
}

/**
 * 作为文件导出
 */
fun exportAsFile(
    saveFilePath: String?
) {
    logger.info { "saveFilePath $saveFilePath" }
    if (saveFilePath == null) return
    File(saveFilePath).bufferedWriter().use { writer ->
        ProcessRunnerManager.multiTaskFlow.value.onEach { entry ->
            writer.write(entry.key)
            writer.newLine()
            if (entry.value.isEmpty()) {
                writer.write("   无输出结果")
                writer.newLine()
            } else {
                entry.value.onEach {
                    writer.write("   $it")
                    writer.newLine()
                }
            }
            writer.write("-------------------------")
            writer.newLine()
            writer.newLine()
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TaskLogger(value: UiContentPage) {
    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
            ContentPageController.navigateToPageByIndex(TaskLoggerPage())
        }.background(
            if (value is TaskLoggerPage) Color.White else Color(0xfff4f4f4)
        )
    ) {
        Text(
            "Task Log",
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(16.0F, TextUnitType.Sp),
            fontWeight = FontWeight.W100,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun TaskLoggerPageUi() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Magenta)) {
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Logcat(value: UiContentPage) {
    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
            ContentPageController.navigateToPageByIndex(LogcatPage())
        }.background(
            if (value is LogcatPage) Color.White else Color(0xfff4f4f4)
        )
    ) {
        Text(
            "Logcat",
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(16.0F, TextUnitType.Sp),
            fontWeight = FontWeight.W100,
            fontFamily = FontFamily.Monospace
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun LogcatUi() {
    val scope = rememberCoroutineScope()
    var list = LogcatManager.logcatCacheStateFlow.collectAsState()
    var lineMax = LogcatManager.logcatMaxCounterStateFlow.collectAsState()

    var selectedIndex by remember { mutableStateOf(-1) }
    val processList by LogcatManager.processFlow.collectAsState()

    var priorityIndex by remember { mutableStateOf(0) }
    val priorityList = listOf("Verbose", "Debug", "Info", "Warn", "Error", "Assert")

    val fontSize = remember { mutableStateOf(11.0f) }

    Column(modifier = Modifier.fillMaxSize().absolutePadding(bottom = 30.dp)) {
        val state = rememberLazyListState()

        val state2 = rememberScrollState()

        Row(modifier = Modifier.wrapContentWidth()) {
            Button(onClick = {
                ProcessRunnerManager.startLogcat()
            }) {
                Text("执行")
            }

            Button(onClick = {
                LogcatManager.stopLogcatOutput()
            }) {
                Text("暂停output")
            }

            Button(onClick = {
                LogcatManager.startLogcatOutput()
            }) {
                Text("恢复output")
            }

            Button(onClick = {
                LogcatManager.clearAllCacheData()
            }) {
                Text("清除缓存数据")
            }

            Button(onClick = {
                fontSize.value = fontSize.value + 1
            }) {
                Text("字体变大")
            }

            Button(onClick = {
                fontSize.value = fontSize.value - 1
            }) {
                Text("字体变小")
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.width(150.dp)) {
                var expanded by remember { mutableStateOf(false) }

                Button(onClick = {
                    expanded = true
                }) {
                    Text(if (selectedIndex == -1) "No Debuggable Process" else "${processList[selectedIndex].packageName}(${processList[selectedIndex].pid}")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(300.dp).height(200.dp).background(color = Color.White)
                ) {
                    processList.forEachIndexed { index, process ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            expanded = false
                        }) {
                            Text(
                                text = "${process.packageName}(${process.pid})", fontSize = TextUnit(
                                    12.0f, TextUnitType.Sp
                                ), lineHeight = TextUnit(20.0f, TextUnitType.Sp), modifier = Modifier.height(20.dp)
                            )
                        }
                    }
                }
            }


            Box(modifier = Modifier.width(150.dp)) {
                var expanded2 by remember { mutableStateOf(false) }

                Button(onClick = {
                    expanded2 = true
                }) {
                    Text(if (priorityIndex == -1) "Verbose" else priorityList[priorityIndex])
                }

                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                    modifier = Modifier.width(300.dp).height(200.dp).background(color = Color.White)
                ) {
                    priorityList.forEachIndexed { index, str ->
                        DropdownMenuItem(onClick = {
                            priorityIndex = index
                            expanded2 = false
                        }) {
                            Text(
                                text = str, fontSize = TextUnit(
                                    12.0f, TextUnitType.Sp
                                ), lineHeight = TextUnit(20.0f, TextUnitType.Sp), modifier = Modifier.height(20.dp)
                            )
                        }
                    }
                }
            }
        }

        Box {
            LazyColumn(state = state) {
                items(list.value.filter {
                    if (selectedIndex == -1)
                        true
                    else
                        processList[selectedIndex].pid == it?.pid
                }.filter {
                    filterByPriority(priority = priorityList[priorityIndex], tag = it?.priority ?: "V")
                }) {
                    Text(
                        text = it.toString(),
                        modifier = Modifier.horizontalScroll(state = state2).width((lineMax.value * 10).dp)
                            .padding(top = 3.dp, bottom = 3.dp),
                        maxLines = 1,
                        fontSize = TextUnit(fontSize.value, TextUnitType.Sp),
                        letterSpacing = TextUnit(0.3f, TextUnitType.Sp),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                ),
                style = ScrollbarStyle(
                    minimalHeight = 150.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(0.dp),
                    hoverDurationMillis = 300,
                    unhoverColor = Color.Black.copy(alpha = 0.12f),
                    hoverColor = Color.Black.copy(alpha = 0.50f)
                )
            )
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().height(20.dp),
                adapter = rememberScrollbarAdapter(
                    scrollState = state2
                ), style = ScrollbarStyle(
                    minimalHeight = 150.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(0.dp),
                    hoverDurationMillis = 300,
                    unhoverColor = Color.Black.copy(alpha = 0.12f),
                    hoverColor = Color.Black.copy(alpha = 0.50f)
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

    }

}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Setting(value: UiContentPage) {
    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
            ContentPageController.navigateToPageByIndex(SettingPage())
        }.background(
            if (value is SettingPage) Color.White else Color(0xfff4f4f4)
        )
    ) {
        Text(
            "Setting",
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(16.0F, TextUnitType.Sp),
            fontWeight = FontWeight.W100,
            fontFamily = FontFamily.Monospace
        )
    }
}


@Preview
@Composable
fun dropdownMenuTest() {
    val expandState = remember {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            expandState.value = true
        }) {
        Text(text = "打开 DropdownMenu")
    }
}


@Composable
fun dropdownMenuItemTest(state: MutableState<Boolean>, icon: ImageVector, text: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressState = interactionSource.collectIsPressedAsState()
    val focusState = interactionSource.collectIsFocusedAsState()
    DropdownMenuItem(
        onClick = {
            state.value = false
        },
        enabled = true,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (pressState.value || focusState.value) Color.Red else Color.Black
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp),
            color = if (pressState.value || focusState.value) Color.Red else Color.Black
        )
    }
}


@Composable
fun TaskLogPageUi() {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().height(20.dp).background(TaskLogBarBg)) {
            IconButton(onClick = {
                ContentPageController.navigateToPageByIndex(TaskPage())
            }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    painter = painterResource("images/hide.png"),
                    contentDescription = null,
                    tint = BottomAppBarTaskLog
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            val state = rememberLazyListState()

            LazyColumn(modifier = Modifier.fillMaxSize(), state) {
                // Add a single item
                item {
                    Text(text = "First ")
                }

                // Add 5 items
                items(1000) { index ->
                    TaskLogPageUiListItem("${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())} - Item: $index")
                }

                // Add another single item
                item {
                    Text(text = "Last item")
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }
}

@Composable
fun TaskLogPageUiListItem(
    content: String
) {
    Row {
        Text(text = content, modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp))
        Checkbox(checked = false, onCheckedChange = {})

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomePageUi() {
    var offsetX by remember { mutableStateOf(200f) }
    var active by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Red)
    ) {
//        Column {
//            Button(onClick = {
//                GlobalScope.launch {
//                    println("点击开始")
//                    ScreenRecord.startScreenRecordByUi()
//                }
//            }) {
//                Text("开始录像")
//            }

//            Button(onClick = {
//                GlobalScope.launch {
//                    println("点击结束")
//                    ScreenRecord.stopScreenRecordByUi()
//                }
//            }) {
//                Text("结束录像")
//            }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().heightIn(min = 20.dp)) {
            Spacer(modifier = Modifier.height(20.dp).fillMaxWidth()
                .background(color = if (active) Color(0xffd1d1d1) else Color(0xfff4f4f4)).draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        offsetX -= delta
                    }
                ).onPointerEvent(PointerEventType.Enter) { active = true }
                .onPointerEvent(PointerEventType.Exit) { active = false })
            Box(
                modifier = Modifier.fillMaxWidth().height(offsetX.dp).background(Color.Yellow)
                    .align(Alignment.BottomCenter)
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
//fun main() = application {
//    var isOpen by remember { mutableStateOf(true) }
//    if (isOpen) {
//        val state2 = rememberWindowState(placement = WindowPlacement.Floating)
//        Window(
//            onCloseRequest = ,
//            title = "Transparent Window Example",
//            state = state2,
//        ) {
//            BackMoon()
//        }
//    }
//}

@Composable
fun TT(
    bottomSheetState: BottomSheetScaffoldState
) {
    var sheetPeekHeight by remember { mutableStateOf(100.0) }
    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetState,
        topBar = {
            Text("This is SheetContent of ", modifier = Modifier.height(20.dp))
        },
        sheetContent = {
            // Sheet content
            Box(modifier = Modifier.height(400.dp).background(Color.Yellow)) {
                Text("This is SheetContent of ")
            }

        }
    ) {

        LazyColumn {
            items(1000) { index ->
                TaskLogPageUiListItem("${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())} - Item: $index")
            }
        }
        Text("sssssssssssssssssssssssssssssss")
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))
        Text("sssssssssssssssssssssssssssssss", modifier = Modifier.padding(top = 40.dp))

    }
}



