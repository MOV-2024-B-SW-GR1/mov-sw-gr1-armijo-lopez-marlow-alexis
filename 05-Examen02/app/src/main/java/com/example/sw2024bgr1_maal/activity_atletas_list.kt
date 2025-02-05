package com.example.sw2024bgr1_maal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class activity_atletas_list : AppCompatActivity(), AtletaAdapter.AtletaClickListener {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var atletaAdapter: AtletaAdapter
    private var eventoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atletas_list)

        dbHelper = DatabaseHelper(this)
        eventoId = intent.getIntExtra(MainActivity.EXTRA_EVENTO_ID, 0)
        setupViews()
        loadAtletas()
    }

    private fun setupViews() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAtletas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        atletaAdapter = AtletaAdapter(ArrayList(), this)
        recyclerView.adapter = atletaAdapter

        findViewById<Button>(R.id.btnNuevoAtleta).setOnClickListener {
            val intent = Intent(this, activity_atleta_form::class.java)
            intent.putExtra(EXTRA_EVENTO_ID, eventoId)
            startActivityForResult(intent, REQUEST_NUEVO_ATLETA)
        }
    }

    private fun loadAtletas() {
        val atletas = dbHelper.getAtletasByEventoId(eventoId)
        atletaAdapter.updateAtletas(atletas)
    }

    override fun onAtletaClick(atleta: Atleta) {
        showAtletaOptionsDialog(atleta)
    }

    private fun showAtletaOptionsDialog(atleta: Atleta) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.activity_dialog_atleta_options, null))
            .create()

        dialog.show()

        dialog.findViewById<Button>(R.id.btnEditarAtleta)?.setOnClickListener {
            val intent = Intent(this, activity_atleta_form::class.java)
            intent.putExtra(EXTRA_ATLETA_ID, atleta.id)
            intent.putExtra(EXTRA_EVENTO_ID, atleta.eventoId)
            intent.putExtra(EXTRA_ATLETA_NOMBRE, atleta.nombre)
            intent.putExtra(EXTRA_ATLETA_EDAD, atleta.edad)
            intent.putExtra(EXTRA_ATLETA_REGISTRADO, atleta.estaRegistrado)
            intent.putExtra(EXTRA_ATLETA_MEJOR_MARCA, atleta.mejorMarca)
            startActivityForResult(intent, REQUEST_EDITAR_ATLETA)
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnEliminarAtleta)?.setOnClickListener {
            showDeleteConfirmationDialog(atleta)
            dialog.dismiss()
        }
    }

    private fun showDeleteConfirmationDialog(atleta: Atleta) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.activity_dialog_confirm_delete, null))
            .create()

        dialog.show()

        dialog.findViewById<Button>(R.id.btnConfirmar)?.setOnClickListener {
            dbHelper.deleteAtleta(atleta.id)
            loadAtletas()
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btnCancelar)?.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == REQUEST_NUEVO_ATLETA || requestCode == REQUEST_EDITAR_ATLETA)) {
            loadAtletas()
        }
    }

    companion object {
        const val REQUEST_NUEVO_ATLETA = 1
        const val REQUEST_EDITAR_ATLETA = 2
        const val EXTRA_ATLETA_ID = "extra_atleta_id"
        const val EXTRA_ATLETA_NOMBRE = "extra_atleta_nombre"
        const val EXTRA_ATLETA_EDAD = "extra_atleta_edad"
        const val EXTRA_ATLETA_REGISTRADO = "extra_atleta_registrado"
        const val EXTRA_ATLETA_MEJOR_MARCA = "extra_atleta_mejor_marca"
        const val EXTRA_EVENTO_ID = "extra_evento_id"
    }
}