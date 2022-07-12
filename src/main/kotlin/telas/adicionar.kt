package telas

import Store
import Stores
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.google.gson.Gson
import navigation.NavController
import java.io.File

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun addScreen(
    NavController: NavController,
    stores: Stores,
    edit: MutableState<Int>
){
    val nome = remember {
        if(edit.value!= stores.setores.size){
            mutableStateOf(stores.setores[edit.value].Nome)
        }else{
            mutableStateOf("")
        }
    }
    val attach = remember {
        if(edit.value!= stores.setores.size){
            mutableStateOf(stores.setores[edit.value].Code)
        }else{
            mutableStateOf(Key.A)
        }
    }
    val cor = remember {
        if(edit.value!=stores.setores.size){
            mutableStateOf(stores.setores[edit.value].Cor)
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
                    stores.setores.forEachIndexed { _, sector ->
                        if(sector.Code==it.key){
                            text.value=""
                            pass.value=false
                        }
                    }
                    if(pass.value){
                        attach.value = it.key
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
                if(edit.value!=stores.setores.size){
                    stores.setores[edit.value].Nome = nome.value
                    stores.setores[edit.value].Code = attach.value
                    stores.setores[edit.value].Cor = cor.value
                    val gson = Gson()
                    val caminho = "./medicos.data"
                    val arquivo = File(caminho)
                    val jsonString:String = gson.toJson(stores)
                    arquivo.writeText(jsonString)
                }else{
                    val temp = arrayListOf("")
                    temp.remove("")
                    val m = Store(nome.value, attach.value, cor.value,temp,null)
                    stores.setores.add(m)
                    val gson = Gson()
                    val calamine = "./medicos.data"
                    val arquillian = File(calamine)
                    val jsonString:String = gson.toJson(stores)
                    arquillian.writeText(jsonString)
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