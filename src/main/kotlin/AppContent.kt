import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Content() {
    Column {
        Button(onClick = {
            ConnectManager.doConnect()
        }) {
            Text("ConnectManager.doConnect()")
        }

        Button(onClick = {
            ConnectManager.disConnect()
        }) {
            Text("ConnectManager.disConnect()")
        }
    }

}