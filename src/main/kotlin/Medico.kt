import java.io.Serializable

class Medico(Nome: String, Setor:String, Cor:String) {
    val Nome:String = Nome
    val Setor:String = Setor
    val Cor:String = Cor
    val Atendimentos:Array<String>? = null

    fun get(param:String): Serializable? {
        return when{
            param.contains("nome",ignoreCase=true) -> this.Nome
            param.contains("setor",ignoreCase=true) -> this.Setor
            param.contains("cor",ignoreCase=true) -> this.Cor
            param.contains("atendimentos",ignoreCase=true) -> this.Atendimentos
            else -> {
                null
            }
        }
    }
    fun addPac(pac:String): Unit? {
        this.Atendimentos?.set(this.Atendimentos.size,pac)
        return null
    }

}