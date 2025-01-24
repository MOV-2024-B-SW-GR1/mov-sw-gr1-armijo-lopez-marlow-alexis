package DEBERIIB

data class Evento(
    val id: Int = 0,        // El ID se manejará como autoincrementable
    val nombre: String,
    val fecha: String,     // Puedes usar un tipo Date si prefieres trabajar con fechas
    val capacidad: Int,
    val cuota: Double
)
