//IMPORTAÇÕES
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.File
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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
data class Setor(val Nome:String, val Code:String, val Cor:Color, val Medicos:ArrayList<String>?, var Atendimento:ArrayList<String>?) {}
data class Config(var textSize: TextUnit){}
//FUNÇÕES DE TELA
@Composable
@Preview
fun App(setores: Setores, agora_PAC: MutableState<String>, agora_MED: MutableState<Setor>, edit:MutableState<Int>) {
    val screens = Screens.values().toList()
    val navcontroller by rememberNavControlle(Screens.Index.label)
    val currentScreen by remember {
        navcontroller.currentScreen
    }
    val atendimento by remember { mutableStateOf(arrayListOf(Setor("","",Color.Yellow,null,null))) }
    atendimento.remove(Setor("","",Color.Yellow,null,null))
    MaterialTheme {
        Box(Modifier.fillMaxSize(),Alignment.TopCenter ){
            CustomNavigationHost(NavController=navcontroller,setores,atendimento,agora_PAC,agora_MED,edit)
        }
    }
}
@Composable
@Preview
/*fun Call(
    agora_PAC: MutableState<String>, agora_MED: MutableState<Medico>,
    configuracao:Config
) {
    val atendimento by remember { mutableStateOf(arrayListOf(Medico("",null))) }
    atendimento.remove(Medico("",null))
    MaterialTheme {
        if(agora_PAC.value.isBlank() || agora_PAC.value.isEmpty() || agora_MED.value.nome.isBlank() || agora_MED.value.nome.isEmpty()){
            Box(Modifier.fillMaxSize(),Alignment.TopCenter ){
                Image(
                    painter = painterResource("logo.ico"),
                    "Logo",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }else{
            Box(Modifier.fillMaxSize().background(agora_MED.value.cor),Alignment.TopCenter ){
                Column(Modifier.fillMaxSize()){
                    Text("Dr ${agora_MED.value.medico?.Nome}",Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center, fontSize = configuracao.textSize)
                    Text("${agora_MED.value.nome}",Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center, fontSize = configuracao.textSize)
                    Text("Paciente - ${agora_PAC.value}",Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center, fontSize = configuracao.textSize)
                }
            }
        }
    }
}*/
fun Menuzin(isAskingToClose:MutableState<Boolean>,configuracao:Config) {
    Dialog(
        onCloseRequest = { isAskingToClose.value = false },
        title = "Tamanho da letra",
    ) {
        Column{
            var text by remember { mutableStateOf(configuracao.textSize.value) }
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
                    configuracao.textSize = text.sp
                    val config = "./medicos.conf"
                    val arq_conf = File(config)
                    var jsonString:String = gson.toJson(configuracao)
                    arq_conf.writeText(jsonString)
                }
            ) {
                Text("Salvar")
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
    atendimento: ArrayList<Setor>,
    agora_PAC: MutableState<String>,
    agora_MED: MutableState<Setor>,
    edit: MutableState<Int>
){
    NavigationHost(NavController){
        composable(Screens.Index.label){
            IndexScreen(NavController, setores, atendimento,edit)
        }
        composable(Screens.Index2.label){
            Index2Screen(NavController, setores, atendimento,edit)
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
fun main() = application {
    val gson:Gson = Gson()
    var setores:Setores = Setores(arrayListOf(Setor("","",Color.Yellow,null,null)))
    var configuracao:Config = Config(10.sp)
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
        configuracao = Config(post.textSize)
    }
    //VARIAVEIS PARA ATENDIMENTO/EDITAR
    val atendimento by remember { mutableStateOf(arrayListOf(Setor("","",Color.Black,null,null))) }
    atendimento.remove(Setor("","",Color.Black,null,null))
    val agora_PAC = mutableStateOf("")
    val agora_MED = mutableStateOf(Setor("","",Color.Black,null,null))
    val edit = mutableStateOf(setores.Setores.size)
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
            Menu("Configurações",mnemonic='C') {
                Item("Tamanho da letra", onClick = {
                    isAskingToClose.value = true
                })
            }
        }
        if (isAskingToClose.value) {
            Menuzin(isAskingToClose,configuracao)
        }
        App(setores,agora_PAC,agora_MED,edit)
    }
    //TELA ATENDIMENTO
    /*Window(
        onCloseRequest = ::exitApplication,
        title = "Chamada",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        icon = icon
    ) {
        Call(agora_PAC,agora_MED,configuracao)
    }*/
}