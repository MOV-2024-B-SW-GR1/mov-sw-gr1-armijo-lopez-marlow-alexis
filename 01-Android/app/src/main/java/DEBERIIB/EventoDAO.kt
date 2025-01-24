package DEBERIIB

import android.content.ContentValues
import android.content.Context

class EventoDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertEvento(evento: Evento): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", evento.nombre)
            put("fecha", evento.fecha)
            put("capacidad", evento.capacidad)
            put("cuota", evento.cuota)
        }
        return db.insert("Evento", null, values)
    }

    fun getAllEventos(): List<Evento> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Evento", null)
        val eventos = mutableListOf<Evento>()

        if (cursor.moveToFirst()) {
            do {
                val evento = Evento(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    capacidad = cursor.getInt(cursor.getColumnIndexOrThrow("capacidad")),
                    cuota = cursor.getDouble(cursor.getColumnIndexOrThrow("cuota"))
                )
                eventos.add(evento)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return eventos
    }

    fun updateEvento(evento: Evento): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", evento.nombre)
            put("fecha", evento.fecha)
            put("capacidad", evento.capacidad)
            put("cuota", evento.cuota)
        }
        return db.update("Evento", values, "id = ?", arrayOf(evento.id.toString()))
    }

    fun deleteEvento(eventoId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete("Evento", "id = ?", arrayOf(eventoId.toString()))
    }
}