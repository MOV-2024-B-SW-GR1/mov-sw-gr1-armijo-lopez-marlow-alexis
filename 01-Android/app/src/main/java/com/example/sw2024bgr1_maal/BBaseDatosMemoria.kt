package com.example.sw2024bgr1_maal

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador= arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Marlow","marlow@gmail.com"))
            arregloBEntrenador.add(BEntrenador(2,"Alexis","alexis@gmail.com"))
            arregloBEntrenador.add(BEntrenador(3,"Armijo","armijo@gmail.com"))
        }
    }
}