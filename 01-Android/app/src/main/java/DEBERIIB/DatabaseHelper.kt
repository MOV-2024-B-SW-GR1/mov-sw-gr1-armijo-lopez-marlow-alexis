package DEBERIIB


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "eventosDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createEventoTable = """
            CREATE TABLE Evento (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                fecha TEXT NOT NULL,
                capacidad INTEGER NOT NULL,
                cuota REAL NOT NULL
            )
        """

        val createAtletaTable = """
            CREATE TABLE Atleta (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                edad INTEGER NOT NULL,
                estaRegistrado INTEGER NOT NULL,
                mejorMarca REAL NOT NULL,
                eventoId INTEGER NOT NULL,
                FOREIGN KEY(eventoId) REFERENCES Evento(id)
            )
        """

        db.execSQL(createEventoTable)
        db.execSQL(createAtletaTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Evento")
        db.execSQL("DROP TABLE IF EXISTS Atleta")
        onCreate(db)
    }
}
