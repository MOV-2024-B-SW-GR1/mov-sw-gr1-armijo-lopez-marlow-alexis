package DEBERIIB

class Atleta(
    val id: Int = 0,       // ID autoincrementable
    var nombre: String,
    var edad: Int,
    var estaRegistrado: Boolean,
    var mejorMarca: Double,
    var eventoId: Int      // Relación con el ID de Evento
)
