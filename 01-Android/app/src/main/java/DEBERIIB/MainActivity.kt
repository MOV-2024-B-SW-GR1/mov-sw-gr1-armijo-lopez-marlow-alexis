package DEBERIIB

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sw2024bgr1_maal.R

class MainActivity : AppCompatActivity() {
    private lateinit var eventoDAO: EventoDAO
    private lateinit var adapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventoDAO = EventoDAO(this)
        val eventos = eventoDAO.getAllEventos()

        val listView: ListView = findViewById(R.id.listViewEventos)
        adapter = EventoAdapter(this, eventos)
        listView.adapter = adapter

        val btnCrearEvento: Button = findViewById(R.id.btnCrearEvento)
        btnCrearEvento.setOnClickListener {
            val intent = Intent(this, CrearEventoActivity::class.java)
            startActivity(intent)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val evento = eventos[position]
            showEventoOptions(evento)
        }
    }

    private fun showEventoOptions(evento: Evento) {
        val options = arrayOf("Editar", "Eliminar", "Ver Atletas")
        AlertDialog.Builder(this)
            .setTitle("Opciones para ${evento.nombre}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, EditarEventoActivity::class.java)
                        intent.putExtra("eventoId", evento.id)
                        startActivity(intent)
                    }
                    1 -> {
                        eventoDAO.deleteEvento(evento.id)
                        refreshEventos()
                    }
                    2 -> {
                        val intent = Intent(this, VerAtletasActivity::class.java)
                        intent.putExtra("eventoId", evento.id)
                        startActivity(intent)
                    }
                }
            }
            .show()
    }

    private fun refreshEventos() {
        val eventos = eventoDAO.getAllEventos()
        adapter.updateEventos(eventos)
    }
}
