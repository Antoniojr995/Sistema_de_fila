package navigation

import androidx.compose.runtime.Composable

class NavigationHost(
    val NavController:NavController,
    val contents:@Composable NavigationGraphBuilder.() -> Unit
){
    @Composable
    fun build(){
        NavigationGraphBuilder().renderContents()
    }
    inner class NavigationGraphBuilder(
        val NavController: NavController = this@NavigationHost.NavController
    ){
        @Composable
        fun renderContents(){
            this@NavigationHost.contents(this)
        }
    }
}
@Composable
fun NavigationHost.NavigationGraphBuilder.composable(
    route:String,
    content: @Composable () -> Unit
){
    if(NavController.currentScreen.value==route){
        content()
    }
}