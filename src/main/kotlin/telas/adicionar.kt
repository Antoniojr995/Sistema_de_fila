package telas

import Medico
import Medicos
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import navigation.NavController
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddScreen(
    NavController: NavController,
    medicos:Medicos
){
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
                var m = Medico("Pedrox","Pediatria", Color.Red,null)
                medicos.Medicos.add(m)
                val gson: Gson = Gson()
                val caminho = "./medicos.json"
                val arquivo = File(caminho)
                var jsonString:String = gson.toJson(medicos)
                arquivo.writeText(jsonString)
                NavController.navigate("Inicio")
                             }, modifier = Modifier.width(100.dp).height(100.dp),shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(Color.Green)) {
                Text("Salvar",textAlign = TextAlign.Center, fontSize = 30.sp)
            }
        }
    }
}