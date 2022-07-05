package telas

import Setor
import Setores
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.google.gson.Gson
import navigation.NavController
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
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
            mutableStateOf(setores.Setores[edit.value].Code+"")
        }else{
            mutableStateOf("")
        }
    }
    val cor = remember {
        if(edit.value!=setores.Setores!!.size){
            mutableStateOf(setores.Setores[edit.value].Cor)
        }else{
            mutableStateOf(Color.Red)
        }
    }
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
                modifier = Modifier.padding(10.dp),
                value = atalho.value,
                onValueChange = {
                    atalho.value = it
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
                    setores.Setores[edit.value].Code = atalho.value.get(0)
                    setores.Setores[edit.value].Cor = cor.value
                    val gson: Gson = Gson()
                    val caminho = "./medicos.data"
                    val arquivo = File(caminho)
                    var jsonString:String = gson.toJson(setores)
                    arquivo.writeText(jsonString)
                }else{
                    var temp = arrayListOf("")
                    temp.remove("")
                    var m = Setor(nome.value, atalho.value.get(0), cor.value,temp,null)
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
}