//IMPORTAÇÕES
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.File
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.compose.ui.window.MenuBar
import com.google.gson.Gson
import navigation.NavController
import navigation.NavigationHost
import navigation.composable
import navigation.rememberNavControlle
import telas.*
import java.io.BufferedReader

//CLASSES DE DATA
data class Setores(val Setores:ArrayList<Setor>){}
data class Setor(var Nome:String, var Code: Key, var Cor:Color, var Medicos:ArrayList<String>?, var Atendimento:ArrayList<String>?) {}
data class Config(var textSize: TextUnit){}
//FUNÇÕES DE TELA
@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(setores: Setores,
        agora_PAC: MutableState<String>,
        agora_MED: MutableState<Setor>,
        edit:MutableState<Int>,
        AtendimentoStart:MutableState<Boolean>,
        atendimento: MutableState<ArrayList<Setor>>
) {
    val screens = Screens.values().toList()
    val navcontroller by rememberNavControlle(Screens.Index.label)
    val currentScreen by remember {
        navcontroller.currentScreen
    }
    val requester = remember { FocusRequester() }
    MaterialTheme {
        Box(
            Modifier.fillMaxSize(),
            Alignment.TopCenter
        ){
            CustomNavigationHost(NavController=navcontroller,setores,atendimento,agora_PAC,agora_MED,edit,AtendimentoStart)
        }
    }
}
@Composable
@Preview
fun Call(
    agora_PAC: MutableState<String>, agora_MED: MutableState<Setor>,
    configuracao:MutableState<Config>, change: MutableState<Boolean>
) {
    MaterialTheme {
        if(agora_PAC.value.isBlank() || agora_PAC.value.isEmpty() || agora_MED.value.Medicos!!.get(0).isBlank() || agora_MED.value.Medicos!!.get(0).isEmpty()){
            Box(Modifier.fillMaxSize(),Alignment.TopCenter ){
                Image(
                    painter = painterResource("logo.ico"),
                    "Logo",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }else{
            if(change.value){
                Box(Modifier.fillMaxSize().background(agora_MED.value.Cor),Alignment.TopCenter ){
                    Column(Modifier.fillMaxSize()){
                        Text("Dr ${agora_MED.value.Medicos?.get(0)}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuracao.value.textSize)
                        Text("${agora_MED.value.Nome}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuracao.value.textSize)
                        Text("Paciente - ${agora_PAC.value}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuracao.value.textSize)
                    }
                }
            }else{
                Box(Modifier.fillMaxSize().background(agora_MED.value.Cor),Alignment.TopCenter ){
                    Column(Modifier.fillMaxSize()){
                        Text("Dr ${agora_MED.value.Medicos?.get(0)}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuracao.value.textSize)
                        Text("${agora_MED.value.Nome}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuracao.value.textSize)
                        Text("Paciente - ${agora_PAC.value}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuracao.value.textSize)
                    }
                }
            }
        }
    }
}
//NAVEGAÇÃO
enum class Screens(
    val label:String,
){
    Index(
        label = "Inicio"
    ),
    Index2(
        label = "Inicio2"
    ),
    Add(
        label = "Adicionar"
    ),
    Atendimento(
        label = "Atendimento"
    ),
    Atendimento2(
        label = "Atendimento2"
    )
}
@Composable
fun CustomNavigationHost(
    NavController:NavController,
    setores:Setores,
    atendimento: MutableState<ArrayList<Setor>>,
    agora_PAC: MutableState<String>,
    agora_MED: MutableState<Setor>,
    edit: MutableState<Int>,
    AtendimentoStart: MutableState<Boolean>
){
    NavigationHost(NavController){
        composable(Screens.Index.label){
            IndexScreen(NavController, setores, atendimento,edit,AtendimentoStart)
        }
        composable(Screens.Index2.label){
            Index2Screen(NavController, setores, atendimento,edit,AtendimentoStart)
        }
        composable(Screens.Add.label){
            AddScreen(NavController, setores,edit)
        }
        composable(Screens.Atendimento.label){
            AtendimentoScreen(NavController,atendimento,agora_PAC,agora_MED,setores)
        }
        composable(Screens.Atendimento2.label){
            Atendimento2Screen(NavController,atendimento,agora_PAC,agora_MED,setores)
        }
    }.build()
}
//FUNÇÃO RUN
@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val gson:Gson = Gson()
    var setores:Setores = Setores(arrayListOf(Setor("",Key.A,Color.Yellow,arrayListOf(""),null)))
    setores.Setores.remove(Setor("",Key.A,Color.Yellow,arrayListOf(""),null))
    var configuracao = remember { mutableStateOf(Config(10.sp)) }
    //VERIFICA/CRIAR ARQUIVO DAPA FUNCIONAR O SISTEMA
    val caminho = "./medicos.data"
    val config = "./medicos.conf"
    val arquivo = File(caminho)
    val arq_conf = File(config)
    val isNewFileCreated :Boolean = arquivo.createNewFile()
    val isNewConfigCreated :Boolean = arq_conf.createNewFile()
    if(isNewFileCreated){
        var jsonString:String = gson.toJson(setores)
        arquivo.writeText(jsonString)
    } else{
        var bufferedReader: BufferedReader = arquivo.bufferedReader()
        var inputString = bufferedReader.use { it.readText() }
        var post = gson.fromJson(inputString, Setores::class.java)
        setores = Setores(post.Setores)
    }
    if(isNewConfigCreated){
        var jsonString:String = gson.toJson(configuracao)
        arq_conf.writeText(jsonString)
        println("$caminho is created successfully.")
    } else{
        var bufferedReader: BufferedReader = arq_conf.bufferedReader()
        var inputString = bufferedReader.use { it.readText() }
        var post = gson.fromJson(inputString, Config::class.java)
        configuracao.value = Config(post.textSize)
    }
    //VARIAVEIS PARA ATENDIMENTO/EDITAR
    val atendimento = remember { mutableStateOf(arrayListOf(Setor("",Key.A,Color.Black,null,null))) }
    atendimento.value.remove(Setor("",Key.A,Color.Black,null,null))
    val agora_PAC = mutableStateOf("")
    val agora_MED = mutableStateOf(Setor("",Key.A,Color.Black,null,null))
    val edit = mutableStateOf(setores.Setores.size)
    val AtendimentoStart = remember { mutableStateOf(false) }
    val change = remember { mutableStateOf(false) }
    //ICONE DO APP
    val icon = painterResource("logo.ico")
    //TELA ADM
    Window(
        onCloseRequest = ::exitApplication,
        title = "Adiministração",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        icon = icon
    ) {
        var isAskingToClose = remember { mutableStateOf(false) }
        MenuBar{
            Menu("Configurações",mnemonic='M') {
                Item("Tamanho da letra", onClick = {
                    isAskingToClose.value = true
                })
            }
        }
        if (isAskingToClose.value) {
            Menuzin(isAskingToClose,configuracao,change)
        }
        App(setores,agora_PAC,agora_MED,edit,AtendimentoStart,atendimento)
    }
    //TELA ATENDIMENTO
    if(AtendimentoStart.value){
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chamada",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            icon = icon
        ) {
            MenuBar{

            }
            Call(agora_PAC,agora_MED,configuracao,change)
        }
    }
}
@Composable
fun Menuzin(isAskingToClose:MutableState<Boolean>,configuracao:MutableState<Config>,change:MutableState<Boolean>) {
    Dialog(
        onCloseRequest = { isAskingToClose.value = false },
        title = "Tamanho da letra",
    ) {
        Column{
            var text by remember { mutableStateOf(configuracao.value.textSize.value) }
            TextField(
                value = text.toString(),
                onValueChange = {
                    text = it.toFloat()
                },
                label = { Text("Tamanho") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Text("A", fontSize = text.sp)
            Button(
                onClick = {
                    val gson:Gson = Gson()
                    isAskingToClose.value = false
                    configuracao.value.textSize = text.sp
                    val config = "./medicos.conf"
                    val arq_conf = File(config)
                    var jsonString:String = gson.toJson(configuracao.value)
                    arq_conf.writeText(jsonString)
                    change.value = !change.value
                }
            ) {
                Text("Salvar")
            }
        }
    }
}