import java.io.Serializable
import Medico
data class Medicos(val Medicos:ArrayList<Medico>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Medicos

        if (Medicos != other.Medicos) return false

        return true
    }

    override fun hashCode(): Int {
        return Medicos?.hashCode() ?: 0
    }
}