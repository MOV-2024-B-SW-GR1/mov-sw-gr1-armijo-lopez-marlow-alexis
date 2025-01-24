package DEBERIIB

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sw2024bgr1_maal.R

class CrearEventoActivity : AppCompatActivity() {
    private lateinit var eventoDAO: EventoDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)

        eventoDAO = EventoDAO(this)

        val etNombre: EditText = findViewById(R.id.etNombreEvento)
        val etFecha: EditText = findViewById(R.id.etFechaEvento)
        val etCapacidad: EditText = findViewById(R.id.etCapacidadEvento)
        val etCuota: EditText = findViewById(R.id.etCuotaEvento)
        val btnGuardar: Button = findViewById(R.id.btnGuardarEvento)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val fecha = etFecha.text.toString()
            val capacidad = etCapacidad.text.toString().toIntOrNull() ?: 0
            val cuota = etCuota.text.toString().toDoubleOrNull() ?: 0.0

            if (nombre.isNotEmpty() && fecha.isNotEmpty() && capacidad > 0 && cuota > 0) {
                val evento = Evento(nombre = nombre, fecha = fecha, capacidad = capacidad, cuota = cuota)
                eventoDAO.insertEvento(evento)
                Toast.makeText(this, "Evento creado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}