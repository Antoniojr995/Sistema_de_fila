import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class Medico(val Nome:String, val Setor:String, val Cor: Color, var Atendimentos:ArrayList<String>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Medico

        if (Nome != other.Nome) return false
        if (Setor != other.Setor) return false
        if (Cor != other.Cor) return false
        if (Atendimentos != other.Atendimentos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Nome.hashCode()
        result = 31 * result + Setor.hashCode()
        result = 31 * result + Cor.hashCode()
        result = 31 * result + (Atendimentos?.hashCode() ?: 0)
        return result
    }
}