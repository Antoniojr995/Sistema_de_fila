// Copyright 2000-2021 J etBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.io.File
import Medico
import Medicos
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.google.gson.Gson
import java.io.BufferedReader

@OptIn(ExperimentalFoundationApi::class, ExperimentalUnitApi::class)
@Composable
@Preview
fun App(medicos:Medicos) {
    var count = remember { mutableStateOf(0) }
    MaterialTheme {
        LazyVerticalGrid(cells = GridCells.Adaptive(100.dp), contentPadding = PaddingValues(5.dp), horizontalArrangement = Arrangement.spacedBy(5.dp), verticalArrangement = Arrangement.spacedBy(5.dp)){
            items(medicos.Medicos!!.size){ medico ->
                Card(modifier = Modifier.width(100.dp).height(100.dp),shape = RoundedCornerShape(20.dp), backgroundColor = medicos.Medicos[medico].Cor){
                    Column(Modifier.padding(0.dp,10.dp,0.dp,0.dp)){
                        Text(medicos.Medicos[medico].Nome,Modifier.align(Alignment.CenterHorizontally),textAlign = TextAlign.Center)
                        Text(medicos.Medicos[medico].Setor,Modifier.align(Alignment.CenterHorizontally),textAlign = TextAlign.Center)
                    }
                }
            }
            item{
                Button(onClick = {
                    count.value++
                },modifier = Modifier.width(100.dp).height(100.dp),shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(Color.Green)) {
                    Text("+",textAlign = TextAlign.Center, fontSize = 30.sp)
                }
            }
        }
    }
}
fun main() = application {
    val gson:Gson = Gson()
    var medic_list:Medicos = Medicos(arrayListOf(Medico("Douglas","Pediatria", Color.Yellow,null)))
    val caminho = "./medicos.json"
    val arquivo = File(caminho)
    val isNewFileCreated :Boolean = arquivo.createNewFile()
    //VERIFICA SE O ARQUIVO FOI CRIADO
    if(isNewFileCreated){
        var jsonString:String = gson.toJson(medic_list)
        arquivo.writeText(jsonString)
        println("$caminho is created successfully.")
    } else{
        //ARQUIVO JÁ EXISTE
        //CARREGAR AS INFORMÇÕES
        var bufferedReader: BufferedReader = arquivo.bufferedReader()
        var inputString = bufferedReader.use { it.readText() }
        var post = gson.fromJson(inputString, Medicos::class.java)
        medic_list = Medicos(post.Medicos)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Teste",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        App(medic_list)
    }
}

