import java.io.File

data class Atleta(
    var id: Int,
    var nombre: String,
    var edad: Int,
    var estaRegistrado: Boolean,
    var mejorMarca: Double
)

class AtletaManager(private val eventoManager: EventoManager) {
    private val archivoAtletas = "atletas.txt"
    private val archivoConfig = "config_atletas.txt"
    private var ultimoIdAtleta = 0

    init {
        cargarConfig()
        cargarAtletas()
    }

    private fun cargarConfig() {
        try {
            if (File(archivoConfig).exists()) {
                val config = File(archivoConfig).readLines()
                ultimoIdAtleta = config[0].toInt()
            }
        } catch (e: Exception) {
            println("Error al cargar configuración de atletas: ${e.message}")
            ultimoIdAtleta = 0
        }
    }

    private fun guardarConfig() {
        try {
            File(archivoConfig).writeText("$ultimoIdAtleta")
        } catch (e: Exception) {
            println("Error al guardar configuración de atletas: ${e.message}")
        }
    }

    private fun cargarAtletas() {
        try {
            if (File(archivoAtletas).exists()) {
                File(archivoAtletas).useLines { lineas ->
                    lineas.forEach { linea ->
                        val partes = linea.split("|")
                        if (partes.size == 6) {
                            val idEvento = partes[0].toInt()
                            val evento = eventoManager.obtenerEventoPorId(idEvento)
                            evento?.atletas?.add(
                                Atleta(
                                    id = partes[1].toInt(),
                                    nombre = partes[2],
                                    edad = partes[3].toInt(),
                                    estaRegistrado = partes[4].toBoolean(),
                                    mejorMarca = partes[5].toDouble()
                                )
                            )
                        }
                    }
                }
            }
            println("Atletas cargados exitosamente.")
        } catch (e: Exception) {
            println("Error al cargar atletas: ${e.message}")
        }
    }

    fun guardarAtletas() {
        try {
            File(archivoAtletas).bufferedWriter().use { writer ->
                eventoManager.eventos.forEach { evento ->
                    evento.atletas.forEach { atleta ->
                        writer.write("${evento.id}|${atleta.id}|${atleta.nombre}|${atleta.edad}|${atleta.estaRegistrado}|${atleta.mejorMarca}\n")
                    }
                }
            }
            guardarConfig()
            println("Atletas guardados exitosamente.")
        } catch (e: Exception) {
            println("Error al guardar los atletas: ${e.message}")
        }
    }

    fun registrarAtleta() {
        println("\n=== Registrar Nuevo Atleta ===")
        eventoManager.listarEventos()
        print("\nSeleccione el ID del evento: ")
        val idEvento = readLine()!!.toInt()

        val evento = eventoManager.obtenerEventoPorId(idEvento)
        if (evento == null) {
            println("Evento no encontrado!")
            return
        }

        print("Nombre del atleta: ")
        val nombre = readLine()!!
        print("Edad: ")
        val edad = readLine()!!.toInt()
        print("¿Está registrado? (si/no): ")
        val estaRegistrado = readLine()!!.lowercase() == "si"
        print("Mejor marca personal (en Km): ")
        val mejorMarca = readLine()!!.toDouble()

        val atleta = Atleta(++ultimoIdAtleta, nombre, edad, estaRegistrado, mejorMarca)
        evento.atletas.add(atleta)
        guardarAtletas()
        println("\nAtleta registrado exitosamente!")
    }

    fun actualizarAtleta() {
        println("\n=== Actualizar Atleta ===")
        eventoManager.listarEventos()
        print("\nSeleccione el ID del evento: ")
        val idEvento = readLine()!!.toInt()

        val evento = eventoManager.obtenerEventoPorId(idEvento)
        if (evento == null) {
            println("Evento no encontrado!")
            return
        }

        if (evento.atletas.isEmpty()) {
            println("No hay atletas registrados en este evento.")
            return
        }

        evento.atletas.forEach { println("${it.id}: ${it.nombre}") }
        print("\nSeleccione el ID del atleta: ")
        val idAtleta = readLine()!!.toInt()

        val atleta = evento.atletas.find { it.id == idAtleta }
        if (atleta == null) {
            println("Atleta no encontrado!")
            return
        }

        println("\nIngrese los nuevos datos (presione Enter para mantener el valor actual):")
        print("Nombre (${atleta.nombre}): ")
        val nombre = readLine()
        if (!nombre.isNullOrEmpty()) atleta.nombre = nombre

        print("Edad (${atleta.edad}): ")
        val edad = readLine()
        if (!edad.isNullOrEmpty()) atleta.edad = edad.toInt()

        print("Registrado (${if (atleta.estaRegistrado) "si" else "no"}): ")
        val registrado = readLine()
        if (!registrado.isNullOrEmpty()) {
            when (registrado.lowercase()) {
                "si" -> atleta.estaRegistrado = true
                "no" -> atleta.estaRegistrado = false
                else -> println("Valor inválido. No se actualizará el estado de registro.")
            }
        }

        print("Mejor marca (${atleta.mejorMarca}): ")
        val mejorMarca = readLine()
        if (!mejorMarca.isNullOrEmpty()) {
            try {
                atleta.mejorMarca = mejorMarca.toDouble()
            } catch (e: NumberFormatException) {
                println("Error: Valor inválido. No se actualizará la mejor marca.")
            }
        }

        guardarAtletas()
        println("\nAtleta actualizado exitosamente!")
    }

    fun eliminarAtleta() {
        println("\n=== Eliminar Atleta ===")
        eventoManager.listarEventos()
        print("\nSeleccione el ID del evento: ")
        val idEvento = readLine()!!.toInt()

        val evento = eventoManager.obtenerEventoPorId(idEvento)
        if (evento == null) {
            println("Evento no encontrado!")
            return
        }

        if (evento.atletas.isEmpty()) {
            println("No hay atletas registrados en este evento.")
            return
        }

        evento.atletas.forEach { println("${it.id}: ${it.nombre}") }
        print("\nSeleccione el ID del atleta a eliminar: ")
        val idAtleta = readLine()!!.toInt()

        val atletaEliminado = evento.atletas.removeIf { it.id == idAtleta }
        if (atletaEliminado) {
            guardarAtletas()
            println("Atleta eliminado exitosamente!")
        } else {
            println("Atleta no encontrado!")
        }
    }

    fun listarAtletas(idEvento: Int) {
        val evento = eventoManager.obtenerEventoPorId(idEvento)
        if (evento == null) {
            println("Evento no encontrado.")
            return
        }

        println("\n=== Lista de Atletas en ${evento.nombre} ===")
        if (evento.atletas.isEmpty()) {
            println("No hay atletas registrados en este evento.")
            return
        }

        evento.atletas.forEach { atleta ->
            println("\nID: ${atleta.id}")
            println("Nombre: ${atleta.nombre}")
            println("Edad: ${atleta.edad}")
            println("Registrado: ${if (atleta.estaRegistrado) "Sí" else "No"}")
            println("Mejor marca: ${atleta.mejorMarca} Km")
        }
    }
}
