import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun Content() {
    Column {
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