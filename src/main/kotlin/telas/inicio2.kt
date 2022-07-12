package telas

import Store
import Stores
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
import kotlin.collections.ArrayList

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun index2Screen(
    NavController: NavController,
    stores:Stores,
    atonement: MutableState<ArrayList<Store>>,
    edit: MutableState<Int>,
    AtonementStart: MutableState<Boolean>
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
            items(stores.setores.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(400.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = stores.setores[medico].Cor
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
                                    stores.setores.remove(stores.setores[medico])
                                    if(atonement.value.size>0){
                                        atonement.value.remove(stores.setores[medico])
                                    }
                                    edit.value = stores.setores.size
                                    val gson = Gson()
                                    val calamine = "./medicos.data"
                                    val arquillian = File(calamine)
                                    val jsonString:String = gson.toJson(stores)
                                    arquillian.writeText(jsonString)
                                    NavController.navigate("Inicio")
                                },
                                modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                            ){
                                Icon(
                                    Icons.Rounded.Delete, contentDescription = "Mudar"
                                )
                            }
                        }
                        Text(
                            stores.setores[medico].Nome,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Medicos",
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        stores.setores[medico].Medicos?.forEachIndexed { index, s ->
                            Row(Modifier.width(100.dp).align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.SpaceBetween) {
                                if (index > 0 && stores.setores[medico].Medicos!!.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val aux = stores.setores[medico].Medicos?.get(index)
                                            stores.setores[medico].Medicos?.set(
                                                index,
                                                stores.setores[medico].Medicos?.get(index - 1) ?: ""
                                            )
                                            if (aux != null) {
                                                stores.setores[medico].Medicos?.set(index - 1, aux)
                                            }
                                            NavController.navigate("Inicio")
                                        },
                                        modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                    ) {
                                        Icon(
                                            Icons.Rounded.KeyboardArrowUp, contentDescription = "Mudar"
                                        )
                                    }
                                }
                                if (index < stores.setores[medico].Medicos!!.size - 1 && stores.setores[medico].Medicos!!.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val aux = stores.setores[medico].Medicos?.get(index)
                                            stores.setores[medico].Medicos?.set(
                                                index,
                                                stores.setores[medico].Medicos?.get(index + 1) ?: ""
                                            )
                                            if (aux != null) {
                                                stores.setores[medico].Medicos?.set(index + 1, aux)
                                            }
                                            NavController.navigate("Inicio")
                                        },
                                        modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                    ) {
                                        Icon(
                                            Icons.Rounded.KeyboardArrowDown, contentDescription = "Mudar"
                                        )
                                    }
                                }
                                Text(
                                    s,
                                    textAlign = TextAlign.Center
                                )
                                IconButton(
                                    onClick = {
                                        stores.setores[medico].Medicos?.remove(
                                            stores.setores[medico].Medicos!![index]
                                        )
                                        NavController.navigate("Inicio")
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
                        if (!atonement.value.contains(stores.setores[medico]) && stores.setores[medico].Medicos!!.size>0) {
                            Button(
                                onClick = {
                                    atonement.value.add(stores.setores[medico])
                                    NavController.navigate("Inicio")
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
                        edit.value = stores.setores.size
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
            items(atonement.value.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = atonement.value[medico].Cor
                ) {
                    Column(Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
                        Text(
                            atonement.value[medico].Nome,
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            atonement.value[medico].Medicos?.get(0) ?: "",
                            Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        if(atonement.value[medico].Atendimento.isNullOrEmpty()){
                            Button(
                                onClick = {
                                    if (atonement.value.contains(atonement.value[medico])) {
                                        atonement.value.remove(atonement.value[medico])
                                        NavController.navigate("Inicio")
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
        if(atonement.value.size>0){
            Button(
                onClick = {
                    atonement.value.forEach { medico ->
                        if(medico.Atendimento.isNullOrEmpty()){
                            medico.Atendimento = arrayListOf("")
                            medico.Atendimento!!.remove("")
                        }
                    }
                    AtonementStart.value = true
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
                            stores.setores[info.value].Medicos?.add(nome.value)
                            nome.value = ""
                            val gson = Gson()
                            val calamine = "./medicos.data"
                            val arquillian = File(calamine)
                            val jsonString:String = gson.toJson(stores)
                            arquillian.writeText(jsonString)
                            NavController.navigate("Inicio")
                        }
                    ){
                        Text("Adicionar")
                    }
                }
            )
        }
    }
}