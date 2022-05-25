import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun Content() {
    Column(modifier = Modifier.background(Color.Black).fillMaxSize()) {
        Button(onClick = {
//            ConnectManager.judgeDeviceIsConnected()
        }) {
            Text("ConnectManager.doConnect()")
        }

        Button(onClick = {
//            ConnectManager.judgeDeviceIsDisConnect()
        }) {
            Text("ConnectManager.disConnect()")
        }

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

        }


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

