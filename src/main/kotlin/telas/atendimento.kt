package telas

import Store
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import javazoom.jl.player.advanced.AdvancedPlayer
import navigation.NavController
import java.io.FileInputStream


@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun atonementScreen(
    NavController: NavController,
    atonement: MutableState<ArrayList<Store>>,
    agora_PAC: MutableState<String>,
    agora_MED: MutableState<Store>
){
    val opDg = remember { mutableStateOf(false) }
    val info = remember { mutableStateOf(0) }
    val nome = remember { mutableStateOf("") }
    val opMM = remember { mutableStateOf(false) }
    val med = remember { mutableStateOf(0) }
    val requester = remember { FocusRequester() }
    val ver = remember { mutableStateOf(true) }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.onKeyEvent {
            atonement.value.forEachIndexed { index, setor ->
                if(setor.Code==it.key && ver.value){
                    if(atonement.value[index].Atendimento!!.size>0){
                        ver.value=false
                        agora_PAC.value = atonement.value[index].Atendimento!![0]
                        atonement.value[index].Atendimento!!.remove(atonement.value[index].Atendimento!![0])
                        agora_MED.value = atonement.value[index]
                        val audio = "./aviso.mp3"
                        val fis = FileInputStream(audio)
                        val player = AdvancedPlayer(fis)
                        player.play()
                        NavController.navigate("Atendimento2")
                    }
                }
            }
            false
        }
            .focusRequester(requester)
            .focusable()) {
        Button(
            onClick = {
                val audio = "./aviso.mp3"
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
            items(atonement.value.size) { medico ->
                Card(
                    modifier = Modifier.width(150.dp).height(400.dp),
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
                        Button(
                            onClick = {
                                med.value = medico
                                opMM.value = true
                            },
                            modifier = Modifier.align(Alignment.End).fillMaxWidth()
                        ) {
                            Text("Mudar medico")
                        }
                        if(atonement.value[medico].Atendimento!!.size>0){
                            Button(
                                onClick = {
                                    agora_PAC.value = atonement.value[medico].Atendimento!![0]
                                    atonement.value[medico].Atendimento!!.remove(atonement.value[medico].Atendimento!![0])
                                    agora_MED.value = atonement.value[medico]
                                    NavController.navigate("Atendimento2")
                                    val audio = "./aviso.mp3"
                                    val fis = FileInputStream(audio)
                                    val player = AdvancedPlayer(fis)
                                    player.play()
                                },
                                modifier = Modifier.align(Alignment.End).fillMaxWidth()
                            ) {
                                Text("Chamar")
                            }
                        }
                        atonement.value[medico].Atendimento!!.forEachIndexed { it, s ->
                            Row(Modifier.width(100.dp).align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.SpaceBetween){
                                if(it>0 && atonement.value[medico].Atendimento!!.size>1){
                                    IconButton(
                                        onClick = {
                                            val aux = atonement.value[medico].Atendimento?.get(it)
                                            atonement.value[medico].Atendimento?.set(it,
                                                atonement.value[medico].Atendimento?.get(it-1) ?: ""
                                            )
                                            if (aux != null) {
                                                atonement.value[medico].Atendimento?.set(it-1, aux)
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
                                if(it<atonement.value[medico].Atendimento!!.size-1 && atonement.value[medico].Atendimento!!.size>1){
                                    IconButton(
                                        onClick = {
                                            val aux = atonement.value[medico].Atendimento?.get(it)
                                            atonement.value[medico].Atendimento?.set(it,
                                                atonement.value[medico].Atendimento?.get(it+1) ?: ""
                                            )
                                            if (aux != null) {
                                                atonement.value[medico].Atendimento?.set(it+1, aux)
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
                                    "${it+1}° $s",
                                    textAlign = TextAlign.Center
                                )
                                IconButton(
                                    onClick = {
                                        atonement.value[medico].Atendimento?.remove(atonement.value[medico].Atendimento!![it])
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
            item {
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
                            atonement.value[info.value].Atendimento?.add(nome.value)
                            nome.value = ""
                        }
                    ){
                        Text("Adicionar")
                    }
                }
            )
        }
        if(opMM.value){
            AlertDialog(
                onDismissRequest = {
                    opMM.value = false
                },
                title = {
                    Text("Mudar Medico")
                },
                text = {
                    Column {
                        atonement.value[med.value].Medicos?.forEachIndexed { index, s ->
                            Button(
                                onClick = {
                                    val aux = atonement.value[med.value].Medicos?.get(0)
                                    atonement.value[med.value].Medicos?.set(0, s)
                                    if (aux != null) {
                                        atonement.value[med.value].Medicos?.set(index, aux)
                                    }
                                    opMM.value = false
                                }
                            ){
                                Text(s)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            opMM.value = false
                        }
                    ){
                        Text("Cancelar")
                    }
                }
            )
        }
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}