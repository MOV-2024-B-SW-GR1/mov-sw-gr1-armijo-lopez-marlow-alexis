package com.example.sw2024bgr1_maal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var dbHelper: DatabaseHelper
    private var eventoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtener el ID del evento del intent
        eventoId = intent.getIntExtra("EVENTO_ID", -1)

        // Inicializar DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Obtener el fragment del mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Buscar el evento en la base de datos
        val evento = dbHelper.getEventoById(eventoId)

        if (evento != null) {
            // Crear un punto de ubicación con la latitud y longitud del evento
            val eventLocation = LatLng(evento.latitude, evento.longitude)

            // Agregar un marcador en la ubicación del evento
            mMap.addMarker(
                MarkerOptions()
                    .position(eventLocation)
                    .title(evento.nombre)
            )

            // Mover la cámara a la ubicación del evento con zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15f))
        } else {
            Toast.makeText(this, "No se encontró la ubicación del evento", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}