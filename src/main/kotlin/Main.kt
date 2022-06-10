// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import Medico
@Composable
@Preview
fun App() {
    var count = remember { mutableStateOf(0) }
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            Button(modifier = Modifier.align(Alignment.Center),
                onClick = {
                    count.value++
                }) {
                Text(if (count.value == 0) "Hello World" else "Clicked ${count.value}!")
            }
            Button(modifier = Modifier.align(Alignment.Center),
                onClick = {
                    count.value = 0
                }) {
                Text("Reset")
            }
        }
    }
}
fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Teste",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        App()
    }
}
