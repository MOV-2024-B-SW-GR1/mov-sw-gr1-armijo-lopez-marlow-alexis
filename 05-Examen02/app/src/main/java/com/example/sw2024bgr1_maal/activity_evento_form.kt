package com.example.sw2024bgr1_maal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class activity_evento_form : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var eventoId: Int = -1

    private lateinit var etNombre: EditText
    private lateinit var etFecha: EditText
    private lateinit var etCapacidad: EditText
    private lateinit var etCuota: EditText
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var tvCoordinates: TextView
    private lateinit var btnObtenerUbicacion: Button

    private lateinit var locationManager: LocationManager
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_form)

        // Inicializar componentes
        dbHelper = DatabaseHelper(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombreEvento)
        etFecha = findViewById(R.id.etFechaEvento)
        etCapacidad = findViewById(R.id.etCapacidadEvento)
        etCuota = findViewById(R.id.etCuotaEvento)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        tvCoordinates = findViewById(R.id.tvCoordinates)
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion)

        // Configurar listener para obtener ubicación
        btnObtenerUbicacion.setOnClickListener {
            if (checkLocationPermission()) {
                getCurrentLocation()
            } else {
                requestLocationPermission()
            }
        }

        // Verificar si es edición o nuevo evento
        eventoId = intent.getIntExtra(MainActivity.EXTRA_EVENTO_ID, -1)

        if (eventoId != -1) {
            // Modo edición: cargar datos existentes
            val evento = dbHelper.getEventoById(eventoId)
            evento?.let {
                etNombre.setText(it.nombre)
                etFecha.setText(it.fecha)
                etCapacidad.setText(it.capacidad.toString())
                etCuota.setText(it.cuota.toString())
                etLatitud.setText(it.latitude.toString())
                etLongitud.setText(it.longitude.toString())
                currentLatitude = it.latitude
                currentLongitude = it.longitude
                updateCoordinatesDisplay()
            }
        }

        findViewById<Button>(R.id.btnGuardarEvento).setOnClickListener {
            guardarEvento()
        }
    }

    private fun guardarEvento() {
        val nombre = etNombre.text.toString()
        val fecha = etFecha.text.toString()
        val capacidad = etCapacidad.text.toString().toIntOrNull() ?: 0
        val cuota = etCuota.text.toString().toDoubleOrNull() ?: 0.0

        // Obtener latitud y longitud de los campos de texto o usar los valores actuales
        val latitud = etLatitud.text.toString().toDoubleOrNull() ?: currentLatitude
        val longitud = etLongitud.text.toString().toDoubleOrNull() ?: currentLongitude

        val evento = Evento(
            id = eventoId,
            nombre = nombre,
            fecha = fecha,
            capacidad = capacidad,
            cuota = cuota,
            latitude = latitud,
            longitude = longitud
        )

        val result = if (eventoId == -1) {
            // Nuevo evento
            dbHelper.insertEvento(evento)
        } else {
            // Actualizar evento existente
            dbHelper.updateEvento(evento)
        }

        if (result != -1L) {
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el evento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                currentLatitude = it.latitude
                currentLongitude = it.longitude

                // Actualizar campos de texto y display
                etLatitud.setText(currentLatitude.toString())
                etLongitud.setText(currentLongitude.toString())
                updateCoordinatesDisplay()
            } ?: run {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCoordinatesDisplay() {
        tvCoordinates.text = "Lat: $currentLatitude, Long: $currentLongitude"
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}