package telas

import Medico
import Medicos
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import navigation.NavController
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AtendimentoScreen(
    NavController: NavController,
    atendimento:ArrayList<Medico>
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
                        LazyVerticalGrid(
                            cells = GridCells.Adaptive(100.dp),
                            contentPadding = PaddingValues(5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            items(atendimento[medico].Atendimentos!!.size){it ->
                                Text(
                                    atendimento[medico].Atendimentos?.get(it) ?: "",
                                    Modifier.align(Alignment.CenterHorizontally),
                                    textAlign = TextAlign.Center
                                )
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