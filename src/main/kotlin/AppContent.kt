import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import base.resource.TaskLogBarBg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun Content() {
    val state = ContentPageController.pageFlow.collectAsState()
    when(state.value) {
         is HomePage -> HomePageUi()
         is TaskLogPage -> TaskLogPageUi()
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
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().height(30.dp).background(TaskLogBarBg)) {
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Add a single item
                    item {
                        Text(text = "First ")
                    }

                    // Add 5 items
                    items(1000) { index ->
                        TaskLogPageUiListItem("Item: $index")
                    }

                    // Add another single item
                    item {
                        Text(text = "Last item")
                    }
                }
            }
    }
}

@Composable
fun TaskLogPageUiListItem(
    content: String
) {
    Row {
        Text(text = content, modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp))

    }
}

@Composable
fun HomePageUi() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color.Red)
    ) {
        Column {
            Button(onClick = {
                GlobalScope.launch {
                    println("点击开始")
                    ScreenRecord.startScreenRecordByUi()
                }
            }) {
                Text("开始录像")
            }

            Button(onClick = {
                GlobalScope.launch {
                    println("点击结束")
                    ScreenRecord.stopScreenRecordByUi()
                }
            }) {
                Text("结束录像")
            }

            Button(onClick = {
                GlobalScope.launch {
//                    println("导航到新的界面")
                }
            }) {
                Text("导航到新的界面")
            }
        }
    }
}
