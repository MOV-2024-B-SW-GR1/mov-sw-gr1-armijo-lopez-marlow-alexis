package DEBERIIB

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sw2024bgr1_maal.R
import android.content.Intent

import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class VerAtletasActivity : AppCompatActivity() {

    private lateinit var listViewAtletas: ListView
    private lateinit var btnAgregarAtleta: Button

    private lateinit var atletaDAO: AtletaDAO
    private var eventoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_atletas)

        listViewAtletas = findViewById(R.id.listViewAtletas)
        btnAgregarAtleta = findViewById(R.id.btnAgregarAtleta)

        atletaDAO = AtletaDAO(this)

        // Obtener el ID del evento desde el intent
        eventoId = intent.getIntExtra("eventoId", 0)

        // Cargar lista de atletas
        cargarAtletas()

        btnAgregarAtleta.setOnClickListener {
            val intent = Intent(this, CrearAtletaActivity::class.java)
            intent.putExtra("eventoId", eventoId)
            startActivity(intent)
        }

        listViewAtletas.setOnItemClickListener { _, _, position, _ ->
            val atleta = listViewAtletas.adapter.getItem(position) as Atleta
            mostrarOpcionesAtleta(atleta)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarAtletas()
    }

    private fun cargarAtletas() {
        val atletas = atletaDAO.getAtletasByEvento(eventoId)
        if (atletas.isNotEmpty()) {
            val adapter = AtletaAdapter(this, atletas)
            listViewAtletas.adapter = adapter
        } else {
            Toast.makeText(this, "No hay atletas registrados en este evento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarOpcionesAtleta(atleta: Atleta) {
        val opciones = arrayOf("Editar", "Eliminar")

        android.app.AlertDialog.Builder(this)
            .setTitle("Opciones para ${atleta.nombre}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, EditarAtletaActivity::class.java)
                        intent.putExtra("atletaId", atleta.id)
                        startActivity(intent)
                    }
                    1 -> {
                        confirmarEliminacion(atleta)
                    }
                }
            }
            .show()
    }

    private fun confirmarEliminacion(atleta: Atleta) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Eliminar Atleta")
            .setMessage("¿Estás seguro de que deseas eliminar a ${atleta.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                val rowsDeleted = atletaDAO.deleteAtleta(atleta.id)
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Atleta eliminado", Toast.LENGTH_SHORT).show()
                    cargarAtletas()
                } else {
                    Toast.makeText(this, "Error al eliminar el atleta", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
