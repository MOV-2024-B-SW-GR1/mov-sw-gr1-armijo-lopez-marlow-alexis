fun main() {
    val gestorEventos = EventoManager()
    val gestorAtletas = AtletaManager(gestorEventos)
    var opcion: Int

    do {
        println("\n=== SISTEMA DE GESTIÓN DE EVENTOS Y ATLETAS ===")
        println("1. Crear evento")
        println("2. Actualizar evento")
        println("3. Eliminar evento")
        println("4. Registrar atleta")
        println("5. Actualizar atleta")
        println("6. Eliminar atleta")
        println("7. Listar eventos")
        println("8. Listar atletas de un evento")
        println("0. Salir")
        print("\nSeleccione una opción: ")

        opcion = try {
            readLine()!!.toInt()
        } catch (e: Exception) {
            -1
        }

        when (opcion) {
            1 -> gestorEventos.crearEvento()
            2 -> gestorEventos.actualizarEvento()
            3 -> gestorEventos.eliminarEvento()
            4 -> gestorAtletas.registrarAtleta()
            5 -> gestorAtletas.actualizarAtleta()
            6 -> gestorAtletas.eliminarAtleta()
            7 -> gestorEventos.listarEventos()
            8 -> {
                print("\nIngrese el ID del evento: ")
                val idEvento = readLine()!!.toInt()
                gestorAtletas.listarAtletas(idEvento)
            }
            0 -> println("¡Hasta luego!")
            else -> println("Opción inválida!")
        }
    } while (opcion != 0)
}
