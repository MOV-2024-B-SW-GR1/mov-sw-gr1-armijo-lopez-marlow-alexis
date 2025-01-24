package com.example.sw2024bgr1_maal

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_atleta_form : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var atletaId: Int = 0
    private var eventoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atleta_form)

        dbHelper = DatabaseHelper(this)
        setupViews()
    }

    private fun setupViews() {
        val etNombre = findViewById<EditText>(R.id.etNombreAtleta)
        val etEdad = findViewById<EditText>(R.id.etEdadAtleta)
        val etMejorMarca = findViewById<EditText>(R.id.etMejorMarcaAtleta)
        val cbRegistrado = findViewById<CheckBox>(R.id.cbRegistradoAtleta)

        // Obtener el ID del evento al que pertenece el atleta
        eventoId = intent.getIntExtra(activity_atletas_list.EXTRA_EVENTO_ID, 0)

        // Cargar datos si es edición
        atletaId = intent.getIntExtra(activity_atletas_list.EXTRA_ATLETA_ID, 0)
        if (atletaId != 0) {
            etNombre.setText(intent.getStringExtra(activity_atletas_list.EXTRA_ATLETA_NOMBRE))
            etEdad.setText(intent.getIntExtra(activity_atletas_list.EXTRA_ATLETA_EDAD, 0).toString())
            etMejorMarca.setText(intent.getDoubleExtra(activity_atletas_list.EXTRA_ATLETA_MEJOR_MARCA, 0.0).toString())
            cbRegistrado.isChecked = intent.getBooleanExtra(activity_atletas_list.EXTRA_ATLETA_REGISTRADO, false)
        }

        findViewById<Button>(R.id.btnGuardarAtleta).setOnClickListener {
            val atleta = Atleta(
                id = atletaId,
                nombre = etNombre.text.toString(),
                edad = etEdad.text.toString().toInt(),
                estaRegistrado = cbRegistrado.isChecked,
                mejorMarca = etMejorMarca.text.toString().toDouble(),
                eventoId = eventoId
            )

            if (atletaId == 0) {
                dbHelper.insertAtleta(atleta)
            } else {
                dbHelper.updateAtleta(atleta)
            }

            setResult(RESULT_OK)
            finish()
        }
    }
}