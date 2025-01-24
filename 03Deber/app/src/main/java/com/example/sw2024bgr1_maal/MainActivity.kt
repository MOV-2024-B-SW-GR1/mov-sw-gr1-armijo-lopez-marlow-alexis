package com.example.sw2024bgr1_maal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), EventoAdapter.EventoClickListener {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        setupViews()
        loadEventos()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventoAdapter = EventoAdapter(ArrayList(), this)
        recyclerView.adapter = eventoAdapter

        findViewById<Button>(R.id.btnNuevoEvento).setOnClickListener {
            startActivityForResult(
                Intent(this, activity_evento_form::class.java),
                REQUEST_NUEVO_EVENTO
            )
        }
    }

    private fun loadEventos() {
        val eventos = dbHelper.getAllEventos()
        eventoAdapter.updateEventos(eventos)
    }

    override fun onEventoClick(evento: Evento) {
        showEventoOptionsDialog(evento)
    }

    private fun showEventoOptionsDialog(evento: Evento) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.activity_dialog_options, null))
            .create()

        dialog.show()

        dialog.findViewById<Button>(R.id.btnEditar)?.setOnClickListener {
            val intent = Intent(this, activity_evento_form::class.java)
            intent.putExtra(EXTRA_EVENTO_ID, evento.id)
            intent.putExtra(EXTRA_EVENTO_NOMBRE, evento.nombre)
            intent.putExtra(EXTRA_EVENTO_FECHA, evento.fecha)
            intent.putExtra(EXTRA_EVENTO_CAPACIDAD, evento.capacidad)
            intent.putExtra(EXTRA_EVENTO_CUOTA, evento.cuota)
            startActivityForResult(intent, REQUEST_EDITAR_EVENTO)
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnEliminar)?.setOnClickListener {
            showDeleteConfirmationDialog(evento)
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnVerAtletas)?.setOnClickListener {
            val intent = Intent(this, activity_atletas_list::class.java)
            intent.putExtra(EXTRA_EVENTO_ID, evento.id)
            startActivity(intent)
            dialog.dismiss()
        }
    }

    private fun showDeleteConfirmationDialog(evento: Evento) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.activity_dialog_confirm_delete, null))
            .create()

        dialog.show()

        dialog.findViewById<Button>(R.id.btnConfirmar)?.setOnClickListener {
            dbHelper.deleteEvento(evento.id)
            loadEventos()
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnCancelar)?.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == REQUEST_NUEVO_EVENTO || requestCode == REQUEST_EDITAR_EVENTO)) {
            loadEventos()
        }
    }

    companion object {
        const val REQUEST_NUEVO_EVENTO = 1
        const val REQUEST_EDITAR_EVENTO = 2
        const val EXTRA_EVENTO_ID = "extra_evento_id"
        const val EXTRA_EVENTO_NOMBRE = "extra_evento_nombre"
        const val EXTRA_EVENTO_FECHA = "extra_evento_fecha"
        const val EXTRA_EVENTO_CAPACIDAD = "extra_evento_capacidad"
        const val EXTRA_EVENTO_CUOTA = "extra_evento_cuota"
    }
}