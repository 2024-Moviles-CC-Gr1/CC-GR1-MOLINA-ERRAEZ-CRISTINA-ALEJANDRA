package com.example.pinterest

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Inicio3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewImages)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ImagesAdapter(listOf(
            R.drawable.pinterest1,
            R.drawable.pinterest2,
            R.drawable.pinterest3,
            R.drawable.pinterest4,
            R.drawable.pinterest5,
            R.drawable.pinterest6,
            R.drawable.pinterest7,
            R.drawable.pinterest8
        ))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, Inicio3::class.java)) // Redirige a la misma actividad
                    true
                }
                R.id.search -> {
                    startActivity(Intent(this, busqueda1::class.java)) // Redirige a BusquedaActivity
                    true
                }
                R.id.message -> {
                    startActivity(Intent(this, Mensaje::class.java)) // Redirige a MensajesActivity
                    true
                }
                R.id.user -> {
                    startActivity(Intent(this, user::class.java)) // Redirige a UsuarioActivity
                    true
                }
                else -> false
            }
        }
    }
}
