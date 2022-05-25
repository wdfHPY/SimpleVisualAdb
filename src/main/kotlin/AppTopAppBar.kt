import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import base.resource.BottomAppBarBgColor

@Composable
@Preview
fun AppTopBar() {
    TopAppBar(modifier = Modifier.fillMaxWidth().height(24.dp), backgroundColor = BottomAppBarBgColor) {
        Text("TopAppBar")
        Text("TopAppBar2")
        Text("TopAppBar3")
        Text("TopAppBar4")
        Text("TopAppBar5")
    }
}