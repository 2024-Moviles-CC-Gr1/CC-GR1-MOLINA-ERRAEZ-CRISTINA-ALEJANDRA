package com.example.storehub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var storeName: String
    private lateinit var lat: String
    private lateinit var lng: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        storeName = intent.getStringExtra("store_name") ?: ""
        lat = intent.getStringExtra("store_lat") ?: "-0.18350472925111566" // Ajusta el nombre de la clave
        lng = intent.getStringExtra("store_lon") ?: "-78.49321440418827" // Ajusta el nombre de la clave

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Verificar que los datos no sean nulos o inválidos
        if (lat.isNotEmpty() && lng.isNotEmpty()) {
            try {
                val storeLocation = LatLng(lat.toDouble(), lng.toDouble())
                mMap.addMarker(MarkerOptions().position(storeLocation).title(storeName))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15f))
            } catch (e: NumberFormatException) {
                // Manejar errores de formato
                e.printStackTrace()
            }
        }
    }
}