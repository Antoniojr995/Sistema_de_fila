package telas

import Setor
import Setores
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.google.gson.Gson
import javazoom.jl.player.advanced.AdvancedPlayer
import navigation.NavController
import java.io.File
import java.io.FileInputStream

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AddScreen(
    NavController: NavController,
    setores: Setores,
    edit: MutableState<Int>
){
    val nome = remember {
        if(edit.value!=setores.Setores!!.size){
            mutableStateOf(setores.Setores[edit.value].Nome)
        }else{
            mutableStateOf("")
        }
    }
    val atalho = remember {
        if(edit.value!=setores.Setores!!.size){
            mutableStateOf(setores.Setores[edit.value].Code)
        }else{
            mutableStateOf(Key.A)
        }
    }
    val cor = remember {
        if(edit.value!=setores.Setores!!.size){
            mutableStateOf(setores.Setores[edit.value].Cor)
        }else{
            mutableStateOf(Color.Red)
        }
    }
    val text = remember { mutableStateOf("") }
    val requester = remember { FocusRequester() }
    val pass = remember { mutableStateOf(true) }
    Row(verticalAlignment = Alignment.CenterVertically){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            TextField(
                modifier = Modifier.padding(10.dp),
                value = nome.value,
                onValueChange = {
                    nome.value = it
                },
                label = { Text("Nome")}
            )
            TextField(
                modifier = Modifier.padding(10.dp).onKeyEvent {
                    setores.Setores.forEachIndexed { index, setor ->
                        if(setor.Code==it.key){
                            text.value=""
                            pass.value=false
                        }
                    }
                    if(pass.value){
                        atalho.value = it.key
                        pass.value=true
                        true
                    }else{
                        false
                    }
                }
                    .focusRequester(requester)
                    .focusable(),
                value = text.value,
                onValueChange = {
                    if(it.length<=1){
                        text.value = it
                    }
                },
                label = { Text("Tecla de Atalho")}
            )
            ClassicColorPicker(
                modifier = Modifier.width(200.dp).height(200.dp).padding(10.dp),
                color = cor.value,
                onColorChanged = {
                        color: HsvColor ->
                    cor.value = color.toColor()
                }
            )
            Text("Amostra",textAlign = TextAlign.Center, fontSize = 20.sp)
            Card(
                modifier = Modifier.width(100.dp).height(100.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = cor.value
            ){
                Column(Modifier.padding(0.dp,10.dp,0.dp,0.dp)){
                    Text(nome.value,Modifier.align(Alignment.CenterHorizontally),textAlign = TextAlign.Center)
                }
            }
            Button(onClick = {
                if(edit.value!=setores.Setores!!.size){
                    setores.Setores[edit.value].Nome = nome.value
                    setores.Setores[edit.value].Code = atalho.value
                    setores.Setores[edit.value].Cor = cor.value
                    val gson: Gson = Gson()
                    val caminho = "./medicos.data"
                    val arquivo = File(caminho)
                    var jsonString:String = gson.toJson(setores)
                    arquivo.writeText(jsonString)
                }else{
                    var temp = arrayListOf("")
                    temp.remove("")
                    var m = Setor(nome.value, atalho.value, cor.value,temp,null)
                    setores.Setores?.add(m)
                    val gson: Gson = Gson()
                    val caminho = "./medicos.data"
                    val arquivo = File(caminho)
                    var jsonString:String = gson.toJson(setores)
                    arquivo.writeText(jsonString)
                }
                NavController.navigate("Inicio")
            },shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(Color.Green)) {
                Text("Salvar",textAlign = TextAlign.Center, fontSize = 20.sp)
            }
        }
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}