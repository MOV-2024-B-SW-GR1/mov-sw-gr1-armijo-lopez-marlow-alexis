package com.example.sw2024bgr1_maal

class BEntrenador (
    var id:Int,
    var nombre:String,
    var descripcion:String?
){
    override fun toString(): String {
        return "$nombre ${descripcion}"
    }

}