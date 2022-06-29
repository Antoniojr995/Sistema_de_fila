package telas

import Medico
import Medicos
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import navigation.NavController
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndexScreen(
    NavController: NavController,
    medicos:Medicos,
    atendimento: ArrayList<Medico>,
    edit: MutableState<Int>
){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(medicos.Medicos!!.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = medicos.Medicos[medico].Cor
                ) {
                    Column(Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                            IconButton(
                                onClick = {
                                    edit.value = medico
                                    NavController.navigate("Adicionar")
                                },
                                modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                            ){
                                Icon(
                                    Icons.Rounded.Edit, contentDescription = "Mudar"
                                )
                            }

                            IconButton(
                                onClick = {
                                    medicos.Medicos.remove(medicos.Medicos[medico])
                                    if(atendimento.size>0){
                                        atendimento.remove(medicos.Medicos[medico])
                                    }
                                    edit.value = medicos.Medicos.size
                                    val gson: Gson = Gson()
                                    val caminho = "./medicos.data"
                                    val arquivo = File(caminho)
                                    var jsonString:String = gson.toJson(medicos)
                                    arquivo.writeText(jsonString)
                                    NavController.navigate("Inicio2")
                                },
                                modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                            ){
                                Icon(
                                    Icons.Rounded.Delete, contentDescription = "Mudar"
                                )
                            }

                        }
                        Text(
                            medicos.Medicos[medico].Nome,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            medicos.Medicos[medico].Setor,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        if (!atendimento.contains(medicos.Medicos[medico])) {
                            Button(
                                onClick = {
                                    atendimento.add(medicos.Medicos[medico])
                                    NavController.navigate("Inicio2")
                                },
                                modifier = Modifier.align(Alignment.End).fillMaxWidth()
                            ) {
                                Text("Adicionar",textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        edit.value = medicos.Medicos.size
                        NavController.navigate("Adicionar")
                    },
                    modifier = Modifier.width(100.dp).height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(Color.Green)
                ) {
                    Text("+", textAlign = TextAlign.Center, fontSize = 30.sp)
                }
            }
        }
        Text("Medicos que vão atender",textAlign = TextAlign.Center, fontSize = 20.sp)
        LazyVerticalGrid(
            cells = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(atendimento.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = atendimento[medico].Cor
                ) {
                    Column(Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                        Text(
                            atendimento[medico].Nome,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            atendimento[medico].Setor,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        if(atendimento[medico].Atendimentos.isNullOrEmpty()){
                            Button(
                                onClick = {
                                    if (atendimento.contains(atendimento[medico])) {
                                        atendimento.remove(atendimento[medico])
                                        NavController.navigate("Inicio2")
                                    }
                                },
                                modifier = Modifier.align(Alignment.End).fillMaxWidth()
                            ) {
                                Text("Remover")
                            }
                        }
                    }
                }
            }
        }
        if(atendimento.size>0){
            Button(
                onClick = {
                    atendimento.forEach { medico ->
                        if(medico.Atendimentos.isNullOrEmpty()){
                            medico.Atendimentos = arrayListOf("")
                            medico.Atendimentos!!.remove("")
                        }
                    }
                    NavController.navigate("Atendimento")
                },
                modifier = Modifier.align(Alignment.End).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Iniciar Atendimento")
            }
        }
    }
}