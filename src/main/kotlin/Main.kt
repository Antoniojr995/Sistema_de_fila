// Copyright 2000-2021 J etBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.File
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.google.gson.Gson
import navigation.NavController
import navigation.NavigationHost
import navigation.composable
import navigation.rememberNavControlle
import telas.*
import java.io.BufferedReader

@Composable
@Preview
fun App(medicos:Medicos, agora_PAC: MutableState<String>, agora_MED: MutableState<Medico>) {
    val screens = Screens.values().toList()
    val navcontroller by rememberNavControlle(Screens.Index.label)
    val currentScreen by remember {
        navcontroller.currentScreen
    }
    val atendimento by remember { mutableStateOf(arrayListOf(Medico("","",Color.Yellow,null))) }
    atendimento.remove(Medico("","",Color.Yellow,null))
    MaterialTheme {
        Box(Modifier.fillMaxSize(),Alignment.TopCenter ){
            CustomNavigationHost(NavController =navcontroller,medicos,atendimento,agora_PAC,agora_MED)
        }
    }
}
@Composable
@Preview
fun Call(agora_PAC: MutableState<String>, agora_MED: MutableState<Medico>) {
    val atendimento by remember { mutableStateOf(arrayListOf(Medico("","",Color.Yellow,null))) }
    atendimento.remove(Medico("","",Color.Yellow,null))
    MaterialTheme {
        if(agora_PAC.value.isBlank() || agora_PAC.value.isEmpty() || agora_MED.value.Nome.isBlank() || agora_MED.value.Nome.isEmpty()){
            Box(Modifier.fillMaxSize(),Alignment.TopCenter ){
                Text("FOTOS")
            }
        }else{
            Box(Modifier.fillMaxSize().background(agora_MED.value.Cor),Alignment.TopCenter ){
                Column(Modifier.fillMaxSize()){
                    Text("Dr ${agora_MED.value.Nome}",Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center)
                    Text("${agora_MED.value.Setor}",Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center)
                    Text("Paciente - ${agora_PAC.value}",Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center)
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
    val atendimento by remember { mutableStateOf(arrayListOf(Medico("","",Color.Yellow,null))) }
    atendimento.remove(Medico("","",Color.Yellow,null))
    val agora_PAC = mutableStateOf("")
    val agora_MED = mutableStateOf(Medico("","",Color.Yellow,null))
    Window(
        onCloseRequest = ::exitApplication,
        title = "Adiministração",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        App(medic_list,agora_PAC,agora_MED)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Chamada",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        Call(agora_PAC,agora_MED)
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
    ),
    Atendimento2(
        label = "Atendimento2"
    )
}
@Composable
fun CustomNavigationHost(
    NavController:NavController,
    medicos:Medicos,
    atendimento: ArrayList<Medico>,
    agora_PAC: MutableState<String>,
    agora_MED: MutableState<Medico>
){
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
            AtendimentoScreen(NavController,atendimento,agora_PAC,agora_MED)
        }
        composable(Screens.Atendimento2.label){
            Atendimento2Screen(NavController,atendimento,agora_PAC,agora_MED)
        }
    }.build()
}

