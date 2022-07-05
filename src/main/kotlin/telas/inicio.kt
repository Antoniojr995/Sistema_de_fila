package telas

import Setor
import Setores
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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import com.google.gson.Gson
import navigation.NavController
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun IndexScreen(
    NavController: NavController,
    setores:Setores,
    atendimento: MutableState<ArrayList<Setor>>,
    edit: MutableState<Int>,
    AtendimentoStart: MutableState<Boolean>
){
    val opDg = remember { mutableStateOf(false) }
    val info = remember { mutableStateOf(0) }
    val nome = remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(setores.Setores!!.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(400.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = setores.Setores[medico].Cor
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
                                    setores.Setores.remove(setores.Setores[medico])
                                    if(atendimento.value.size>0){
                                        atendimento.value.remove(setores.Setores[medico])
                                    }
                                    edit.value = setores.Setores.size
                                    val gson: Gson = Gson()
                                    val caminho = "./medicos.data"
                                    val arquivo = File(caminho)
                                    var jsonString:String = gson.toJson(setores)
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
                            setores.Setores[medico].Nome,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Medicos",
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        setores.Setores[medico].Medicos?.forEachIndexed { index, s ->
                            Row(Modifier.width(100.dp).align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.SpaceBetween) {
                                if (index > 0 && setores.Setores[medico].Medicos!!.size > 1) {
                                    IconButton(
                                        onClick = {
                                            var aux = setores.Setores[medico].Medicos?.get(index)
                                            setores.Setores[medico].Medicos?.set(
                                                index,
                                                setores.Setores[medico].Medicos?.get(index - 1) ?: ""
                                            )
                                            if (aux != null) {
                                                setores.Setores[medico].Medicos?.set(index - 1, aux)
                                            }
                                            NavController.navigate("Inicio2")
                                        },
                                        modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                    ) {
                                        Icon(
                                            Icons.Rounded.KeyboardArrowUp, contentDescription = "Mudar"
                                        )
                                    }
                                }
                                if (index < setores.Setores[medico].Medicos!!.size - 1 && setores.Setores[medico].Medicos!!.size > 1) {
                                    IconButton(
                                        onClick = {
                                            var aux = setores.Setores[medico].Medicos?.get(index)
                                            setores.Setores[medico].Medicos?.set(
                                                index,
                                                setores.Setores[medico].Medicos?.get(index + 1) ?: ""
                                            )
                                            if (aux != null) {
                                                setores.Setores[medico].Medicos?.set(index + 1, aux)
                                            }
                                            NavController.navigate("Inicio2")
                                        },
                                        modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                    ) {
                                        Icon(
                                            Icons.Rounded.KeyboardArrowDown, contentDescription = "Mudar"
                                        )
                                    }
                                }
                                Text(
                                    "${s}",
                                    textAlign = TextAlign.Center
                                )
                                IconButton(
                                    onClick = {
                                        setores.Setores[medico].Medicos?.remove(
                                            setores.Setores[medico].Medicos!!.get(
                                                index
                                            )
                                        )
                                        NavController.navigate("Inicio2")
                                    },
                                    modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                ) {
                                    Icon(
                                        Icons.Rounded.Delete, contentDescription = "Mudar"
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = {
                                opDg.value = true
                                info.value = medico
                            },
                            modifier = Modifier.align(Alignment.End).fillMaxWidth()
                        ) {
                            Text("Adicionar Medico",textAlign = TextAlign.Center)
                        }
                        if (!atendimento.value.contains(setores.Setores[medico]) && setores.Setores[medico].Medicos!!.size>0) {
                            Button(
                                onClick = {
                                    atendimento.value.add(setores.Setores[medico])
                                    NavController.navigate("Inicio2")
                                },
                                modifier = Modifier.align(Alignment.End).fillMaxWidth()
                            ) {
                                Text("Selecionar",textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        edit.value = setores.Setores.size
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
            items(atendimento.value.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = atendimento.value[medico].Cor
                ) {
                    Column(Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                        Text(
                            atendimento.value[medico].Nome,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            atendimento.value[medico].Medicos?.get(0) ?: "",
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        if(atendimento.value[medico].Atendimento.isNullOrEmpty()){
                            Button(
                                onClick = {
                                    if (atendimento.value.contains(atendimento.value[medico])) {
                                        atendimento.value.remove(atendimento.value[medico])
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
        if(atendimento.value.size>0){
            Button(
                onClick = {
                    atendimento.value.forEach { medico ->
                        if(medico.Atendimento.isNullOrEmpty()){
                            medico.Atendimento = arrayListOf("")
                            medico.Atendimento!!.remove("")
                        }
                    }
                    AtendimentoStart.value = true
                    NavController.navigate("Atendimento")
                },
                modifier = Modifier.align(Alignment.End).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Iniciar Atendimento")
            }
        }
        if(opDg.value){
            AlertDialog(
                onDismissRequest = {
                    opDg.value = false
                    nome.value = ""
                },
                title = {
                    Text("Adicionar Medico")
                },
                text = {
                    TextField(
                        modifier = Modifier.padding(10.dp),
                        value = nome.value,
                        onValueChange = {
                            nome.value = it
                        },
                        label = { Text("Nome do medico")}
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            opDg.value = false
                            setores.Setores[info.value].Medicos?.add(nome.value)
                            nome.value = ""
                            val gson: Gson = Gson()
                            val caminho = "./medicos.data"
                            val arquivo = File(caminho)
                            var jsonString:String = gson.toJson(setores)
                            arquivo.writeText(jsonString)
                            NavController.navigate("Inicio2")
                        }
                    ){
                        Text("Adicionar")
                    }
                }
            )
        }
    }
}