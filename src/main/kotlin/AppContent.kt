import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import base.resource.BottomAppBarTaskLog
import base.resource.TaskLogBarBg
import java.text.SimpleDateFormat
import java.util.logging.Logger

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
    bottomSheetState: BottomSheetScaffoldState
) {
    val state = ContentPageController.pageFlow.collectAsState()
    Row  {
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

        Box(modifier = Modifier.fillMaxSize()) {
            when(state.value) {
                is TaskPage -> TaskPageUi()
                is MultitaskPage -> TaskLogPageUi()
                is TaskLoggerPage -> TaskLoggerPageUi()
                is LogcatPage -> TaskLogPageUi()
                is SettingPage -> TaskLogPageUi()
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TimeArea() {
    Column (modifier = Modifier.fillMaxWidth().height(80.dp).background(Color(0xfff4f4f4))) {
        Box(modifier = Modifier.height(40.dp).fillMaxWidth().align(Alignment.CenterHorizontally)) {
            Text("22:42", modifier = Modifier.align(Alignment.Center), fontSize = TextUnit(28.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
        }

        Box(modifier = Modifier.height(40.dp).fillMaxWidth().align(Alignment.CenterHorizontally)) {
            Text(text = "2022/05/28", modifier = Modifier.align(Alignment.Center), fontSize = TextUnit(20.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Task(
    value: UiContentPage
) {
    Box (modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
        ContentPageController.navigateToPageByIndex(TaskPage())
    }.background(
        if (value is TaskPage) Color.White else Color(0xfff4f4f4)
    )) {
        Text("Task", modifier = Modifier.align(Alignment.Center),fontSize = TextUnit(16.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun TaskPageUi() {
    Box (modifier = Modifier.fillMaxSize().background(Color.Blue)) {
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MultiTask(value: UiContentPage) {
    Box (modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
        ContentPageController.navigateToPageByIndex(MultitaskPage())
    }.background(
        if (value is MultitaskPage) Color.White else Color(0xfff4f4f4)
    )) {
        Text("Multitasking", modifier = Modifier.align(Alignment.Center),fontSize = TextUnit(16.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TaskLogger(value: UiContentPage) {
    Box (modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
        ContentPageController.navigateToPageByIndex(TaskLoggerPage())
    }.background(
        if (value is TaskLoggerPage) Color.White else Color(0xfff4f4f4)
    )) {
        Text("Task Log", modifier = Modifier.align(Alignment.Center),fontSize = TextUnit(16.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun TaskLoggerPageUi() {
    Box (modifier = Modifier.fillMaxSize().background(Color.Magenta)) {
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Logcat(value: UiContentPage) {
    Box (modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
        ContentPageController.navigateToPageByIndex(LogcatPage())
    }.background(
        if (value is LogcatPage) Color.White else Color(0xfff4f4f4)
    )) {
        Text("Logcat", modifier = Modifier.align(Alignment.Center),fontSize = TextUnit(16.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Setting(value: UiContentPage) {
    Box (modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
        ContentPageController.navigateToPageByIndex(SettingPage())
    }.background(
        if (value is SettingPage) Color.White else Color(0xfff4f4f4)
    )) {
        Text("Setting", modifier = Modifier.align(Alignment.Center),fontSize = TextUnit(16.0F, TextUnitType.Sp), fontWeight = FontWeight.W100, fontFamily = FontFamily.Monospace)
    }
}


@Preview
@Composable
fun dropdownMenuTest(){
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
fun dropdownMenuItemTest(state: MutableState<Boolean>, icon: ImageVector, text:String){
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
        Icon(imageVector = icon, contentDescription = text,tint = if(pressState.value || focusState.value) Color.Red else Color.Black)
        Text(text = text,modifier = Modifier.padding(start = 10.dp),color = if(pressState.value || focusState.value) Color.Red else Color.Black)
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
            Spacer(modifier = Modifier.height(20.dp).fillMaxWidth().background(color = if (active) Color(0xffd1d1d1) else Color(0xfff4f4f4)).draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    offsetX -= delta
                }
            ).onPointerEvent(PointerEventType.Enter) { active = true }.onPointerEvent(PointerEventType.Exit) { active = false })
            Box(modifier = Modifier.fillMaxWidth().height(offsetX.dp).background(Color.Yellow).align(Alignment.BottomCenter)) {

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
    var sheetPeekHeight  by remember { mutableStateOf(100.0) }
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



