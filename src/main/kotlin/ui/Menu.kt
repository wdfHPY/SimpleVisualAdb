package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
@Preview
fun TopBar() {
    TopAppBar (modifier = Modifier.fillMaxWidth().height(25.dp), backgroundColor = BarColor) {
        Text("just test")
        Text("just test")
        Text("just test")
        Text("just test")
    }
}