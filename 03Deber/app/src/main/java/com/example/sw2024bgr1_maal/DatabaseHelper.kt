package com.example.sw2024bgr1_maal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "EventosAtletasDB"
        private const val DATABASE_VERSION = 1

        // Tabla Eventos
        private const val TABLE_EVENTOS = "eventos"
        private const val KEY_EVENTO_ID = "id"
        private const val KEY_EVENTO_NOMBRE = "nombre"
        private const val KEY_EVENTO_FECHA = "fecha"
        private const val KEY_EVENTO_CAPACIDAD = "capacidad"
        private const val KEY_EVENTO_CUOTA = "cuota"

        // Tabla Atletas
        private const val TABLE_ATLETAS = "atletas"
        private const val KEY_ATLETA_ID = "id"
        private const val KEY_ATLETA_NOMBRE = "nombre"
        private const val KEY_ATLETA_EDAD = "edad"
        private const val KEY_ATLETA_REGISTRADO = "registrado"
        private const val KEY_ATLETA_MEJOR_MARCA = "mejor_marca"
        private const val KEY_ATLETA_EVENTO_ID = "evento_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createEventosTable = """
            CREATE TABLE $TABLE_EVENTOS (
                $KEY_EVENTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_EVENTO_NOMBRE TEXT,
                $KEY_EVENTO_FECHA TEXT,
                $KEY_EVENTO_CAPACIDAD INTEGER,
                $KEY_EVENTO_CUOTA REAL
            )
        """.trimIndent()

        val createAtletasTable = """
            CREATE TABLE $TABLE_ATLETAS (
                $KEY_ATLETA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_ATLETA_NOMBRE TEXT,
                $KEY_ATLETA_EDAD INTEGER,
                $KEY_ATLETA_REGISTRADO INTEGER,
                $KEY_ATLETA_MEJOR_MARCA REAL,
                $KEY_ATLETA_EVENTO_ID INTEGER,
                FOREIGN KEY($KEY_ATLETA_EVENTO_ID) REFERENCES $TABLE_EVENTOS($KEY_EVENTO_ID)
            )
        """.trimIndent()

        db.execSQL(createEventosTable)
        db.execSQL(createAtletasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ATLETAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTOS")
        onCreate(db)
    }

    // Métodos para Eventos
    fun insertEvento(evento: Evento): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_EVENTO_NOMBRE, evento.nombre)
            put(KEY_EVENTO_FECHA, evento.fecha)
            put(KEY_EVENTO_CAPACIDAD, evento.capacidad)
            put(KEY_EVENTO_CUOTA, evento.cuota)
        }
        return db.insert(TABLE_EVENTOS, null, values)
    }

    fun getAllEventos(): List<Evento> {
        val eventosList = mutableListOf<Evento>()
        val selectQuery = "SELECT * FROM $TABLE_EVENTOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val evento = Evento(
                        id = it.getInt(it.getColumnIndexOrThrow(KEY_EVENTO_ID)),
                        nombre = it.getString(it.getColumnIndexOrThrow(KEY_EVENTO_NOMBRE)),
                        fecha = it.getString(it.getColumnIndexOrThrow(KEY_EVENTO_FECHA)),
                        capacidad = it.getInt(it.getColumnIndexOrThrow(KEY_EVENTO_CAPACIDAD)),
                        cuota = it.getDouble(it.getColumnIndexOrThrow(KEY_EVENTO_CUOTA))
                    )
                    eventosList.add(evento)
                } while (it.moveToNext())
            }
        }
        return eventosList
    }

    fun updateEvento(evento: Evento): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_EVENTO_NOMBRE, evento.nombre)
            put(KEY_EVENTO_FECHA, evento.fecha)
            put(KEY_EVENTO_CAPACIDAD, evento.capacidad)
            put(KEY_EVENTO_CUOTA, evento.cuota)
        }
        return db.update(TABLE_EVENTOS, values, "$KEY_EVENTO_ID = ?", arrayOf(evento.id.toString()))
    }

    fun deleteEvento(eventoId: Int): Int {
        val db = this.writableDatabase
        // Primero eliminamos los atletas asociados
        db.delete(TABLE_ATLETAS, "$KEY_ATLETA_EVENTO_ID = ?", arrayOf(eventoId.toString()))
        // Luego eliminamos el evento
        return db.delete(TABLE_EVENTOS, "$KEY_EVENTO_ID = ?", arrayOf(eventoId.toString()))
    }

    // Métodos para Atletas
    fun insertAtleta(atleta: Atleta): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_ATLETA_NOMBRE, atleta.nombre)
            put(KEY_ATLETA_EDAD, atleta.edad)
            put(KEY_ATLETA_REGISTRADO, if (atleta.estaRegistrado) 1 else 0)
            put(KEY_ATLETA_MEJOR_MARCA, atleta.mejorMarca)
            put(KEY_ATLETA_EVENTO_ID, atleta.eventoId)
        }
        return db.insert(TABLE_ATLETAS, null, values)
    }

    fun getAtletasByEventoId(eventoId: Int): List<Atleta> {
        val atletasList = mutableListOf<Atleta>()
        val selectQuery = "SELECT * FROM $TABLE_ATLETAS WHERE $KEY_ATLETA_EVENTO_ID = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(eventoId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val atleta = Atleta(
                        id = it.getInt(it.getColumnIndexOrThrow(KEY_ATLETA_ID)),
                        nombre = it.getString(it.getColumnIndexOrThrow(KEY_ATLETA_NOMBRE)),
                        edad = it.getInt(it.getColumnIndexOrThrow(KEY_ATLETA_EDAD)),
                        estaRegistrado = it.getInt(it.getColumnIndexOrThrow(KEY_ATLETA_REGISTRADO)) == 1,
                        mejorMarca = it.getDouble(it.getColumnIndexOrThrow(KEY_ATLETA_MEJOR_MARCA)),
                        eventoId = it.getInt(it.getColumnIndexOrThrow(KEY_ATLETA_EVENTO_ID))
                    )
                    atletasList.add(atleta)
                } while (it.moveToNext())
            }
        }
        return atletasList
    }

    fun updateAtleta(atleta: Atleta): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_ATLETA_NOMBRE, atleta.nombre)
            put(KEY_ATLETA_EDAD, atleta.edad)
            put(KEY_ATLETA_REGISTRADO, if (atleta.estaRegistrado) 1 else 0)
            put(KEY_ATLETA_MEJOR_MARCA, atleta.mejorMarca)
            put(KEY_ATLETA_EVENTO_ID, atleta.eventoId)
        }
        return db.update(TABLE_ATLETAS, values, "$KEY_ATLETA_ID = ?", arrayOf(atleta.id.toString()))
    }

    fun deleteAtleta(atletaId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_ATLETAS, "$KEY_ATLETA_ID = ?", arrayOf(atletaId.toString()))
    }
}
