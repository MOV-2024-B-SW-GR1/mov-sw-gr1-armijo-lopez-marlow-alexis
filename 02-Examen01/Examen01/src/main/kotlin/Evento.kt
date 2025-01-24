import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.io.File

data class Evento(
    var id: Int,
    var nombre: String,
    var fecha: LocalDate,
    var capacidad: Int,
    var cuotaInscripcion: Double,
    var atletas: MutableList<Atleta> = mutableListOf()
)

class EventoManager {
    private val archivoEventos = "eventos.txt"
    private val archivoConfig = "config_eventos.txt"
    private val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var ultimoIdEvento = 0
    var eventos = mutableListOf<Evento>()

    init {
        cargarConfig()
        cargarEventos()
    }

    private fun cargarConfig() {
        try {
            if (File(archivoConfig).exists()) {
                val config = File(archivoConfig).readLines()
                ultimoIdEvento = config[0].toInt()
            }
        } catch (e: Exception) {
            println("Error al cargar configuración de eventos: ${e.message}")
            ultimoIdEvento = 0
        }
    }

    private fun guardarConfig() {
        try {
            File(archivoConfig).writeText("$ultimoIdEvento")
        } catch (e: Exception) {
            println("Error al guardar configuración de eventos: ${e.message}")
        }
    }

    fun guardarEventos() {
        try {
            File(archivoEventos).bufferedWriter().use { writer ->
                eventos.forEach { evento ->
                    writer.write("${evento.id}|${evento.nombre}|${evento.fecha}|${evento.capacidad}|${evento.cuotaInscripcion}\n")
                }
            }
            guardarConfig()
            println("Eventos guardados exitosamente.")
        } catch (e: Exception) {
            println("Error al guardar los eventos: ${e.message}")
        }
    }

    private fun cargarEventos() {
        eventos.clear()
        try {
            if (File(archivoEventos).exists()) {
                File(archivoEventos).useLines { lineas ->
                    lineas.forEach { linea ->
                        val partes = linea.split("|")
                        if (partes.size == 5) {
                            eventos.add(
                                Evento(
                                    id = partes[0].toInt(),
                                    nombre = partes[1],
                                    fecha = LocalDate.parse(partes[2]),
                                    capacidad = partes[3].toInt(),
                                    cuotaInscripcion = partes[4].toDouble()
                                )
                            )
                        }
                    }
                }
            }
            println("Eventos cargados exitosamente.")
        } catch (e: Exception) {
            println("Error al cargar eventos: ${e.message}")
            eventos.clear()
        }
    }

    fun crearEvento(): Evento? {
        println("\n=== Crear Nuevo Evento ===")
        print("Nombre del evento: ")
        val nombre = readLine()!!

        val fecha = leerFecha() ?: return null

        print("Capacidad máxima: ")
        val capacidad = readLine()!!.toInt()
        print("Cuota de inscripción: ")
        val cuota = readLine()!!.toDouble()

        val evento = Evento(++ultimoIdEvento, nombre, fecha, capacidad, cuota)
        eventos.add(evento)
        guardarEventos()
        println("\nEvento creado exitosamente!")
        return evento
    }

    fun actualizarEvento() {
        println("\n=== Actualizar Evento ===")
        listarEventos()
        print("Seleccione el ID del evento a actualizar: ")
        val idEvento = readLine()!!.toInt()

        val evento = obtenerEventoPorId(idEvento)
        if (evento == null) {
            println("Evento no encontrado!")
            return
        }

        println("\nIngrese los nuevos datos (presione Enter para mantener el valor actual):")
        print("Nombre (${evento.nombre}): ")
        val nombre = readLine()
        if (!nombre.isNullOrEmpty()) evento.nombre = nombre

        print("Fecha (${evento.fecha.format(formatoFecha)}): ")
        val fechaStr = readLine()
        if (!fechaStr.isNullOrEmpty()) {
            try {
                evento.fecha = LocalDate.parse(fechaStr, formatoFecha)
            } catch (e: DateTimeParseException) {
                println("Formato de fecha inválido. No se actualizará la fecha.")
            }
        }

        print("Capacidad (${evento.capacidad}): ")
        val capacidad = readLine()
        if (!capacidad.isNullOrEmpty()) evento.capacidad = capacidad.toInt()

        print("Cuota (${evento.cuotaInscripcion}): ")
        val cuota = readLine()
        if (!cuota.isNullOrEmpty()) evento.cuotaInscripcion = cuota.toDouble()

        guardarEventos()
        println("\nEvento actualizado exitosamente!")
    }

    fun eliminarEvento() {
        println("\n=== Eliminar Evento ===")
        listarEventos()
        print("Seleccione el ID del evento a eliminar: ")
        val idEvento = readLine()!!.toInt()

        val eventoEliminado = eventos.removeIf { it.id == idEvento }
        if (eventoEliminado) {
            guardarEventos()
            println("Evento eliminado exitosamente!")
        } else {
            println("Evento no encontrado!")
        }
    }

    fun listarEventos() {
        println("\n=== Lista de Eventos ===")
        if (eventos.isEmpty()) {
            println("No hay eventos registrados.")
            return
        }import java.time.LocalDate
        import java.time.format.DateTimeFormatter
                import java.time.format.DateTimeParseException
                import java.io.File

                data class Evento(
            var id: Int,
            var nombre: String,
            var fecha: LocalDate,
            var capacidad: Int,
            var cuotaInscripcion: Double,
            var atletas: MutableList<Atleta> = mutableListOf()
        )

        class EventoManager {
            private val archivoEventos = "eventos.txt"
            private val archivoConfig = "config_eventos.txt"
            private val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var ultimoIdEvento = 0
            var eventos = mutableListOf<Evento>()

            init {
                cargarConfig()
                cargarEventos()
            }

            private fun cargarConfig() {
                try {
                    if (File(archivoConfig).exists()) {
                        val config = File(archivoConfig).readLines()
                        ultimoIdEvento = config[0].toInt()
                    }
                } catch (e: Exception) {
                    println("Error al cargar configuración de eventos: ${e.message}")
                    ultimoIdEvento = 0
                }
            }

            private fun guardarConfig() {
                try {
                    File(archivoConfig).writeText("$ultimoIdEvento")
                } catch (e: Exception) {
                    println("Error al guardar configuración de eventos: ${e.message}")
                }
            }

            fun guardarEventos() {
                try {
                    File(archivoEventos).bufferedWriter().use { writer ->
                        eventos.forEach { evento ->
                            writer.write("${evento.id}|${evento.nombre}|${evento.fecha}|${evento.capacidad}|${evento.cuotaInscripcion}\n")
                        }
                    }
                    guardarConfig()
                    println("Eventos guardados exitosamente.")
                } catch (e: Exception) {
                    println("Error al guardar los eventos: ${e.message}")
                }
            }

            private fun cargarEventos() {
                eventos.clear()
                try {
                    if (File(archivoEventos).exists()) {
                        File(archivoEventos).useLines { lineas ->
                            lineas.forEach { linea ->
                                val partes = linea.split("|")
                                if (partes.size == 5) {
                                    eventos.add(
                                        Evento(
                                            id = partes[0].toInt(),
                                            nombre = partes[1],
                                            fecha = LocalDate.parse(partes[2]),
                                            capacidad = partes[3].toInt(),
                                            cuotaInscripcion = partes[4].toDouble()
                                        )
                                    )
                                }
                            }
                        }
                    }
                    println("Eventos cargados exitosamente.")
                } catch (e: Exception) {
                    println("Error al cargar eventos: ${e.message}")
                    eventos.clear()
                }
            }

            fun crearEvento(): Evento? {
                println("\n=== Crear Nuevo Evento ===")
                print("Nombre del evento: ")
                val nombre = readLine()!!

                val fecha = leerFecha() ?: return null

                print("Capacidad máxima: ")
                val capacidad = readLine()!!.toInt()
                print("Cuota de inscripción: ")
                val cuota = readLine()!!.toDouble()

                val evento = Evento(++ultimoIdEvento, nombre, fecha, capacidad, cuota)
                eventos.add(evento)
                guardarEventos()
                println("\nEvento creado exitosamente!")
                return evento
            }

            fun actualizarEvento() {
                println("\n=== Actualizar Evento ===")
                listarEventos()
                print("Seleccione el ID del evento a actualizar: ")
                val idEvento = readLine()!!.toInt()

                val evento = obtenerEventoPorId(idEvento)
                if (evento == null) {
                    println("Evento no encontrado!")
                    return
                }

                println("\nIngrese los nuevos datos (presione Enter para mantener el valor actual):")
                print("Nombre (${evento.nombre}): ")
                val nombre = readLine()
                if (!nombre.isNullOrEmpty()) evento.nombre = nombre

                print("Fecha (${evento.fecha.format(formatoFecha)}): ")
                val fechaStr = readLine()
                if (!fechaStr.isNullOrEmpty()) {
                    try {
                        evento.fecha = LocalDate.parse(fechaStr, formatoFecha)
                    } catch (e: DateTimeParseException) {
                        println("Formato de fecha inválido. No se actualizará la fecha.")
                    }
                }

                print("Capacidad (${evento.capacidad}): ")
                val capacidad = readLine()
                if (!capacidad.isNullOrEmpty()) evento.capacidad = capacidad.toInt()

                print("Cuota (${evento.cuotaInscripcion}): ")
                val cuota = readLine()
                if (!cuota.isNullOrEmpty()) evento.cuotaInscripcion = cuota.toDouble()

                guardarEventos()
                println("\nEvento actualizado exitosamente!")
            }

            fun eliminarEvento() {
                println("\n=== Eliminar Evento ===")
                listarEventos()
                print("Seleccione el ID del evento a eliminar: ")
                val idEvento = readLine()!!.toInt()

                val eventoEliminado = eventos.removeIf { it.id == idEvento }
                if (eventoEliminado) {
                    guardarEventos()
                    println("Evento eliminado exitosamente!")
                } else {
                    println("Evento no encontrado!")
                }
            }

            fun listarEventos() {
                println("\n=== Lista de Eventos ===")
                if (eventos.isEmpty()) {
                    println("No hay eventos registrados.")
                    return
                }
                eventos.forEach { evento ->
                    println("\nID: ${evento.id}")
                    println("Nombre: ${evento.nombre}")
                    println("Fecha: ${evento.fecha.format(formatoFecha)}")
                    println("Capacidad: ${evento.capacidad}")
                    println("Cuota: $${evento.cuotaInscripcion}")
                    println("Atletas registrados: ${evento.atletas.size}")
                }
            }

            fun obtenerEventoPorId(id: Int): Evento? = eventos.find { it.id == id }

            private fun leerFecha(): LocalDate? {
                while (true) {
                    try {
                        print("Fecha (YYYY-MM-DD): ")
                        val fechaStr = readLine()!!
                        return LocalDate.parse(fechaStr, formatoFecha)
                    } catch (e: DateTimeParseException) {
                        println("Formato de fecha inválido. Use el formato YYYY-MM-DD.")
                        print("¿Desea intentar de nuevo? (si/no): ")
                        if (readLine()?.lowercase() != "si") return null
                    }
                }
            }
        }

        eventos.forEach { evento ->
            println("\nID: ${evento.id}")
            println("Nombre: ${evento.nombre}")
            println("Fecha: ${evento.fecha.format(formatoFecha)}")
            println("Capacidad: ${evento.capacidad}")
            println("Cuota: $${evento.cuotaInscripcion}")
            println("Atletas registrados: ${evento.atletas.size}")
        }
    }

    fun obtenerEventoPorId(id: Int): Evento? = eventos.find { it.id == id }

    private fun leerFecha(): LocalDate? {
        while (true) {
            try {
                print("Fecha (YYYY-MM-DD): ")
                val fechaStr = readLine()!!
                return LocalDate.parse(fechaStr, formatoFecha)
            } catch (e: DateTimeParseException) {
                println("Formato de fecha inválido. Use el formato YYYY-MM-DD.")
                print("¿Desea intentar de nuevo? (si/no): ")
                if (readLine()?.lowercase() != "si") return null
            }
        }
    }
}
