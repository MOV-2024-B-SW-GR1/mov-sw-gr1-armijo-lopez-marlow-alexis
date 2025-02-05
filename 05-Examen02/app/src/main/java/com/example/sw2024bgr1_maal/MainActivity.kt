package com.example.sw2024bgr1_maal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), EventoAdapter.EventoClickListener {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedEventoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        setupViews()
        loadEventos()
    }

    // Agrega el método para crear el menú de opciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Agrega el método para manejar la selección del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_map -> {
                // Verificar si hay un evento seleccionado
                if (selectedEventoId != -1) {
                    val intent = Intent(this, MapsActivity::class.java).apply {
                        putExtra("EVENTO_ID", selectedEventoId)
                    }
                    startActivity(intent)
                } else {
                    // Mostrar un mensaje si no hay evento seleccionado
                    android.widget.Toast.makeText(
                        this,
                        "Seleccione un evento primero",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

        // Seleccionar el primer evento por defecto si hay eventos
        if (eventos.isNotEmpty()) {
            selectedEventoId = eventos[0].id
        }
    }

    override fun onEventoClick(evento: Evento) {
        // Actualizar el evento seleccionado
        selectedEventoId = evento.id
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