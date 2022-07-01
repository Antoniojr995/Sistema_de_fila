package telas

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
import javazoom.jl.player.advanced.AdvancedPlayer
import navigation.NavController
import java.io.FileInputStream


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AtendimentoScreen(
    NavController: NavController,
    atendimento: ArrayList<Medico>,
    agora_PAC: MutableState<String>,
    agora_MED: MutableState<Medico>,
    medicos: Medicos
){
    val opDg = remember { mutableStateOf(false) }
    val info = remember { mutableStateOf(0) }
    val nome = remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                var audio = "./aviso.mp3"
                val fis = FileInputStream(audio)
                val player = AdvancedPlayer(fis)
                player.play()
            },
            modifier = Modifier.align(Alignment.End).fillMaxWidth()
        ) {
            Text("AVISO SONORO")
        }
        LazyVerticalGrid(
            cells = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(atendimento.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(400.dp),
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
                        if(atendimento[medico].Atendimentos!!.size>0){
                            Button(
                                onClick = {
                                    agora_PAC.value = atendimento[medico].Atendimentos!!.get(0)
                                    atendimento[medico].Atendimentos!!.remove(atendimento[medico].Atendimentos!!.get(0))
                                    agora_MED.value = atendimento[medico]
                                    NavController.navigate("Atendimento2")
                                    var audio = "./aviso.mp3"
                                    val fis = FileInputStream(audio)
                                    val player = AdvancedPlayer(fis)
                                    player.play()
                                },
                                modifier = Modifier.align(Alignment.End).fillMaxWidth()
                            ) {
                                Text("Chamar")
                            }
                        }
                        LazyVerticalGrid(
                            cells = GridCells.Adaptive(100.dp),
                            contentPadding = PaddingValues(5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            items(atendimento[medico].Atendimentos!!.size){it ->
                                Row(Modifier.width(100.dp).align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.SpaceBetween){
                                    if(it>0 && atendimento[medico].Atendimentos!!.size>1){
                                        IconButton(
                                            onClick = {
                                                var aux = atendimento[medico].Atendimentos?.get(it)
                                                atendimento[medico].Atendimentos?.set(it,
                                                    atendimento[medico].Atendimentos?.get(it-1) ?: ""
                                                )
                                                if (aux != null) {
                                                    atendimento[medico].Atendimentos?.set(it-1, aux)
                                                }
                                                NavController.navigate("Atendimento2")
                                            },
                                            modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                        ){
                                            Icon(
                                                Icons.Rounded.KeyboardArrowUp, contentDescription = "Mudar"
                                            )
                                        }
                                    }
                                    if(it<atendimento[medico].Atendimentos!!.size-1 && atendimento[medico].Atendimentos!!.size>1){
                                        IconButton(
                                            onClick = {
                                                var aux = atendimento[medico].Atendimentos?.get(it)
                                                atendimento[medico].Atendimentos?.set(it,
                                                    atendimento[medico].Atendimentos?.get(it+1) ?: ""
                                                )
                                                if (aux != null) {
                                                    atendimento[medico].Atendimentos?.set(it+1, aux)
                                                }
                                                NavController.navigate("Atendimento2")
                                            },
                                            modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                        ){
                                            Icon(
                                                Icons.Rounded.KeyboardArrowDown, contentDescription = "Mudar"
                                            )
                                        }
                                    }
                                    Text(
                                        "${it+1}° ${atendimento[medico].Atendimentos?.get(it) ?: ""}",
                                        textAlign = TextAlign.Center
                                    )
                                    IconButton(
                                        onClick = {
                                            atendimento[medico].Atendimentos?.remove(atendimento[medico].Atendimentos!!.get(it))
                                            NavController.navigate("Atendimento2")
                                        },
                                        modifier = Modifier.width(20.dp).height(20.dp).background(Color.Transparent)
                                    ){
                                        Icon(
                                            Icons.Rounded.Delete, contentDescription = "Mudar"
                                        )
                                    }
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
                            Text("Adicionar")
                        }
                    }
                }
            }
            item(){
                Button(
                    onClick = {
                        NavController.navigate("Inicio")
                    },
                    modifier = Modifier.align(Alignment.End).fillMaxWidth()
                ) {
                    Text("Adicionar",textAlign = TextAlign.Center)
                }
            }
        }
        if(opDg.value){
            AlertDialog(
                onDismissRequest = {
                    opDg.value = false
                    nome.value = ""
                },
                title = {
                    Text("Adicionar")
                },
                text = {
                    TextField(
                        modifier = Modifier.padding(10.dp),
                        value = nome.value,
                        onValueChange = {
                            nome.value = it
                        },
                        label = { Text("Nome do paciente")}
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            opDg.value = false
                            atendimento[info.value].Atendimentos?.add(nome.value)
                            nome.value = ""
                        }
                    ){
                        Text("Adicionar")
                    }
                }
            )
        }
    }

}