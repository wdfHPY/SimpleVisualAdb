import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import base.resource.*


@OptIn(ExperimentalUnitApi::class)
val appBottomBarTextUnit = TextUnit(11.5F, TextUnitType.Sp)

@Composable
@Preview
fun AppBottomBar() {
    val connectState = ConnectManager.connectStateFlow.collectAsState()
    BottomAppBar(modifier = Modifier.fillMaxWidth().height(20.dp), backgroundColor = BottomAppBarBgColor) {
        Icon(
            painter = when(connectState.value) {
                is DeviceNoDoConnect -> {
                    painterResource("images/connected.png")
                }
                is DeviceOnline -> {
                    painterResource("images/connected.png")
                }
                is DeviceDisConnect -> {
                    painterResource("images/disconnect.png")
                }
            },
            contentDescription = null,
            tint = when(connectState.value) {
                is DeviceNoDoConnect -> {
                    BottomAppBarNoDoConnect
                }
                is DeviceOnline -> {
                    BottomAppBarConnected
                }
                is DeviceDisConnect -> {
                    BottomAppBarDisConnect
                }
            },
            modifier = Modifier.padding(start = 3.dp)
        )
        Text("BottomAppBar", fontSize = appBottomBarTextUnit, color = BottomAppBarTextColor, modifier = Modifier.padding(start = 20.dp))
        Text("BottomAppBar2")
        Text("BottomAppBar3")
        Text("BottomAppBar4")
        Text("BottomAppBar5")
    }
}