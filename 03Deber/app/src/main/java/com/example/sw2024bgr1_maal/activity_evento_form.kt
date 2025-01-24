package com.example.sw2024bgr1_maal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_evento_form : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var eventoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_form)

        dbHelper = DatabaseHelper(this)
        setupViews()
    }

    private fun setupViews() {
        val etNombre = findViewById<EditText>(R.id.etNombreEvento)
        val etFecha = findViewById<EditText>(R.id.etFechaEvento)
        val etCapacidad = findViewById<EditText>(R.id.etCapacidadEvento)
        val etCuota = findViewById<EditText>(R.id.etCuotaEvento)

        // Cargar datos si es edición
        eventoId = intent.getIntExtra(MainActivity.EXTRA_EVENTO_ID, 0)
        if (eventoId != 0) {
            etNombre.setText(intent.getStringExtra(MainActivity.EXTRA_EVENTO_NOMBRE))
            etFecha.setText(intent.getStringExtra(MainActivity.EXTRA_EVENTO_FECHA))
            etCapacidad.setText(intent.getIntExtra(MainActivity.EXTRA_EVENTO_CAPACIDAD, 0).toString())
            etCuota.setText(intent.getDoubleExtra(MainActivity.EXTRA_EVENTO_CUOTA, 0.0).toString())
        }

        findViewById<Button>(R.id.btnGuardarEvento).setOnClickListener {
            val evento = Evento(
                id = eventoId,
                nombre = etNombre.text.toString(),
                fecha = etFecha.text.toString(),
                capacidad = etCapacidad.text.toString().toInt(),
                cuota = etCuota.text.toString().toDouble()
            )

            if (eventoId == 0) {
                dbHelper.insertEvento(evento)
            } else {
                dbHelper.updateEvento(evento)
            }

            setResult(RESULT_OK)
            finish()
        }
    }
}