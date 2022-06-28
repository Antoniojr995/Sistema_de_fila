package telas

import Medico
import Medicos
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.google.gson.Gson
import navigation.NavController
import java.io.File

@Composable
fun AddScreen(
    NavController: NavController,
    medicos:Medicos,
    edit: MutableState<Int>
){
    val nome = remember {
        if(edit.value!=medicos.Medicos!!.size){
            mutableStateOf(medicos.Medicos[edit.value].Nome)
        }else{
            mutableStateOf("")
        }
    }
    val setor = remember {
        if(edit.value!=medicos.Medicos!!.size){
            mutableStateOf(medicos.Medicos[edit.value].Setor)
        }else{
            mutableStateOf("")
        }
    }
    val cor = remember {
        if(edit.value!=medicos.Medicos!!.size){
            mutableStateOf(medicos.Medicos[edit.value].Cor)
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
                value = setor.value,
                onValueChange = {
                    setor.value = it
                },
                label = { Text("Setor")}
            )
            ClassicColorPicker(
                modifier = Modifier.width(200.dp).height(200.dp).padding(10.dp),
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
                    Text(setor.value,Modifier.align(Alignment.CenterHorizontally),textAlign = TextAlign.Center)
                }
            }
            Button(onClick = {
                if(edit.value!=medicos.Medicos!!.size){
                    medicos.Medicos[edit.value].Nome = nome.value
                    medicos.Medicos[edit.value].Setor = setor.value
                    medicos.Medicos[edit.value].Cor = cor.value
                    val gson: Gson = Gson()
                    val caminho = "./medicos.data"
                    val arquivo = File(caminho)
                    var jsonString:String = gson.toJson(medicos)
                    arquivo.writeText(jsonString)
                }else{
                    var m = Medico(nome.value,setor.value, cor.value,null)
                    medicos.Medicos?.add(m)
                    val gson: Gson = Gson()
                    val caminho = "./medicos.data"
                    val arquivo = File(caminho)
                    var jsonString:String = gson.toJson(medicos)
                    arquivo.writeText(jsonString)
                }
                NavController.navigate("Inicio")
            },shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(Color.Green)) {
                Text("Salvar",textAlign = TextAlign.Center, fontSize = 20.sp)
            }
        }
    }
}