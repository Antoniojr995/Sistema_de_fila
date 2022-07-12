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
data class Stores(val setores:ArrayList<Store>)
data class Store(var Nome:String, var Code: Key, var Cor:Color, var Medicos:ArrayList<String>?, var Atendimento:ArrayList<String>?)
data class Config(var textSize: TextUnit)
//FUNÇÕES DE TELA
@Composable
@Preview
fun app(stores: Stores,
        agora_PAC: MutableState<String>,
        agora_MED: MutableState<Store>,
        edit:MutableState<Int>,
        AtonementStart:MutableState<Boolean>,
        atonement: MutableState<ArrayList<Store>>
) {
    val controller by rememberNavControlle(Screens.Index.label)
    MaterialTheme {
        Box(
            Modifier.fillMaxSize(),
            Alignment.TopCenter
        ){
            customNavigationHost(NavController=controller,stores,atonement,agora_PAC,agora_MED,edit,AtonementStart)
        }
    }
}
@Composable
@Preview
fun call(
    agora_PAC: MutableState<String>, agora_MED: MutableState<Store>,
    configuration:MutableState<Config>, change: MutableState<Boolean>
) {
    MaterialTheme {
        if(agora_PAC.value.isBlank() || agora_PAC.value.isEmpty() || agora_MED.value.Medicos!![0].isBlank() || agora_MED.value.Medicos!![0].isEmpty()){
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
                            textAlign = TextAlign.Center, fontSize = configuration.value.textSize)
                        Text(
                            agora_MED.value.Nome,Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuration.value.textSize)
                        Text("Paciente - ${agora_PAC.value}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuration.value.textSize)
                    }
                }
            }else{
                Box(Modifier.fillMaxSize().background(agora_MED.value.Cor),Alignment.TopCenter ){
                    Column(Modifier.fillMaxSize()){
                        Text("Dr ${agora_MED.value.Medicos?.get(0)}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuration.value.textSize)
                        Text(
                            agora_MED.value.Nome,Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuration.value.textSize)
                        Text("Paciente - ${agora_PAC.value}",Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center, fontSize = configuration.value.textSize)
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
fun customNavigationHost(
    NavController:NavController,
    stores:Stores,
    atonement: MutableState<ArrayList<Store>>,
    agora_PAC: MutableState<String>,
    agora_MED: MutableState<Store>,
    edit: MutableState<Int>,
    AtonementStart: MutableState<Boolean>
){
    NavigationHost(NavController){
        composable(Screens.Index.label){
            indexScreen(NavController, stores, atonement,edit,AtonementStart)
        }
        composable(Screens.Index2.label){
            index2Screen(NavController, stores, atonement,edit,AtonementStart)
        }
        composable(Screens.Add.label){
            addScreen(NavController, stores,edit)
        }
        composable(Screens.Atendimento.label){
            atonementScreen(NavController,atonement,agora_PAC,agora_MED)
        }
        composable(Screens.Atendimento2.label){
            atonement2Screen(NavController,atonement,agora_PAC,agora_MED)
        }
    }.build()
}
//FUNÇÃO RUN
@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val gson = Gson()
    var stores = Stores(arrayListOf(Store("",Key.A,Color.Yellow,arrayListOf(""),null)))
    stores.setores.remove(Store("",Key.A,Color.Yellow,arrayListOf(""),null))
    val configuration = remember { mutableStateOf(Config(10.sp)) }
    //VERIFICA/CRIAR ARQUIVO DAPA FUNCIONAR O SISTEMA
    val calamine = "./medicos.data"
    val config = "./medicos.conf"
    val arquillian = File(calamine)
    val arqConf = File(config)
    val isNewFileCreated :Boolean = arquillian.createNewFile()
    val isNewConfigCreated :Boolean = arqConf.createNewFile()
    if(isNewFileCreated){
        val jsonString:String = gson.toJson(stores)
        arquillian.writeText(jsonString)
    } else{
        val bufferedReader: BufferedReader = arquillian.bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        val post = gson.fromJson(inputString, Stores::class.java)
        stores = Stores(post.setores)
    }
    if(isNewConfigCreated){
        val jsonString:String = gson.toJson(configuration)
        arqConf.writeText(jsonString)
        println("$calamine is created successfully.")
    } else{
        val bufferedReader: BufferedReader = arqConf.bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        val post = gson.fromJson(inputString, Config::class.java)
        configuration.value = Config(post.textSize)
    }
    //VARIAVEIS PARA ATENDIMENTO/EDITAR
    val atendimento = remember { mutableStateOf(arrayListOf(Store("",Key.A,Color.Black,null,null))) }
    atendimento.value.remove(Store("",Key.A,Color.Black,null,null))
    val agoraPac = mutableStateOf("")
    val agoraMed = mutableStateOf(Store("",Key.A,Color.Black,null,null))
    val edit = mutableStateOf(stores.setores.size)
    val booleanMutableState = remember { mutableStateOf(false) }
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
        val isAskingToClose = remember { mutableStateOf(false) }
        MenuBar{
            Menu("Configurações",mnemonic='M') {
                Item("Tamanho da letra", onClick = {
                    isAskingToClose.value = true
                })
            }
        }
        if (isAskingToClose.value) {
            menuzin(isAskingToClose,configuration,change)
        }
        app(stores,agoraPac,agoraMed,edit,booleanMutableState,atendimento)
    }
    //TELA ATENDIMENTO
    if(booleanMutableState.value){
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chamada",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            icon = icon
        ) {
            MenuBar{

            }
            call(agoraPac,agoraMed,configuration,change)
        }
    }
}
@Composable
fun menuzin(isAskingToClose:MutableState<Boolean>, configuracao:MutableState<Config>, change:MutableState<Boolean>) {
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
                    val gson = Gson()
                    isAskingToClose.value = false
                    configuracao.value.textSize = text.sp
                    val config = "./medicos.conf"
                    val arqConf = File(config)
                    val jsonString:String = gson.toJson(configuracao.value)
                    arqConf.writeText(jsonString)
                    change.value = !change.value
                }
            ) {
                Text("Salvar")
            }
        }
    }
}