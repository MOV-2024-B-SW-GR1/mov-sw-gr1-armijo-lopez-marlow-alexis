package com.example.sw2024bgr1_maal

data class Atleta(
    var id: Int = 0,
    var nombre: String,
    var edad: Int,
    var estaRegistrado: Boolean,
    var mejorMarca: Double,
    var eventoId: Int
)