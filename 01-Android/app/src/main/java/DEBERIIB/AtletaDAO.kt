package DEBERIIB

import android.content.ContentValues
import android.content.Context

class AtletaDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertAtleta(atleta: Atleta): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", atleta.nombre)
            put("edad", atleta.edad)
            put("estaRegistrado", if (atleta.estaRegistrado) 1 else 0)
            put("mejorMarca", atleta.mejorMarca)
            put("eventoId", atleta.eventoId)
        }
        return db.insert("Atleta", null, values)
    }

    fun getAtletasByEvento(eventoId: Int): List<Atleta> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Atleta WHERE eventoId = ?", arrayOf(eventoId.toString()))
        val atletas = mutableListOf<Atleta>()

        if (cursor.moveToFirst()) {
            do {
                val atleta = Atleta(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad")),
                    estaRegistrado = cursor.getInt(cursor.getColumnIndexOrThrow("estaRegistrado")) == 1,
                    mejorMarca = cursor.getDouble(cursor.getColumnIndexOrThrow("mejorMarca")),
                    eventoId = cursor.getInt(cursor.getColumnIndexOrThrow("eventoId"))
                )
                atletas.add(atleta)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return atletas
    }

    fun updateAtleta(atleta: Atleta): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", atleta.nombre)
            put("edad", atleta.edad)
            put("estaRegistrado", if (atleta.estaRegistrado) 1 else 0)
            put("mejorMarca", atleta.mejorMarca)
        }
        return db.update("Atleta", values, "id = ?", arrayOf(atleta.id.toString()))
    }

    fun deleteAtleta(atletaId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete("Atleta", "id = ?", arrayOf(atletaId.toString()))
    }
}
