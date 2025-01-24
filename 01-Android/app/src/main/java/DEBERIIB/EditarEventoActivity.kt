package DEBERIIB

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sw2024bgr1_maal.R

class EditarEventoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etFecha: EditText
    private lateinit var etCapacidad: EditText
    private lateinit var etCuota: EditText
    private lateinit var btnGuardar: Button

    private lateinit var eventoDAO: EventoDAO
    private var eventoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_evento)

        etNombre = findViewById(R.id.etNombre)
        etFecha = findViewById(R.id.etFecha)
        etCapacidad = findViewById(R.id.etCapacidad)
        etCuota = findViewById(R.id.etCuota)
        btnGuardar = findViewById(R.id.btnGuardar)

        eventoDAO = EventoDAO(this)

        // Obtener el ID del evento desde el intent
        eventoId = intent.getIntExtra("eventoId", 0)

        // Cargar datos del evento
        cargarDatosEvento()

        btnGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosEvento() {
        val evento = eventoDAO.getAllEventos().find { it.id == eventoId }
        if (evento != null) {
            etNombre.setText(evento.nombre)
            etFecha.setText(evento.fecha)
            etCapacidad.setText(evento.capacidad.toString())
            etCuota.setText(evento.cuota.toString())
        } else {
            Toast.makeText(this, "Evento no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun guardarCambios() {
        val nombre = etNombre.text.toString()
        val fecha = etFecha.text.toString()
        val capacidad = etCapacidad.text.toString().toIntOrNull()
        val cuota = etCuota.text.toString().toDoubleOrNull()

        if (nombre.isBlank() || fecha.isBlank() || capacidad == null || cuota == null) {
            Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val evento = Evento(
            id = eventoId,
            nombre = nombre,
            fecha = fecha,
            capacidad = capacidad,
            cuota = cuota
        )

        val rowsUpdated = eventoDAO.updateEvento(evento)
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Evento actualizado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar el evento", Toast.LENGTH_SHORT).show()
        }
    }
}
