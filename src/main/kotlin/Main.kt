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
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.google.gson.Gson
import navigation.NavController
import navigation.NavigationHost
import navigation.composable
import navigation.rememberNavControlle
import telas.AddScreen
import telas.AtendimentoScreen
import telas.Index2Screen
import telas.IndexScreen
import java.io.BufferedReader

@Composable
@Preview
fun App(medicos:Medicos) {
    val screens = Screens.values().toList()
    val navcontroller by rememberNavControlle(Screens.Index.label)
    val currentScreen by remember {
        navcontroller.currentScreen
    }
    val atendimento by remember { mutableStateOf(arrayListOf(Medico("","",Color.Yellow,null))) }
    atendimento.remove(Medico("","",Color.Yellow,null))
    MaterialTheme {
        Box(Modifier.fillMaxSize(),Alignment.TopCenter ){
            CustomNavigationHost(NavController=navcontroller,medicos,atendimento)
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
enum class Screens(
    val label:String,
){
    Index(
        label = "Inicio"
    ),
    Index2(
        label = "Inicio2"
    ),
    Add(
        label = "Adicionar"
    ),
    Atendimento(
        label = "Atendimento"
    )
}
@Composable
fun CustomNavigationHost(NavController:NavController,medicos:Medicos,atendimento:ArrayList<Medico>){
    NavigationHost(NavController){
        composable(Screens.Index.label){
            IndexScreen(NavController, medicos, atendimento)
        }
        composable(Screens.Index2.label){
            Index2Screen(NavController, medicos, atendimento)
        }
        composable(Screens.Add.label){
            AddScreen(NavController, medicos)
        }
        composable(Screens.Atendimento.label){
            AtendimentoScreen(NavController, atendimento)
        }
    }.build()
}

