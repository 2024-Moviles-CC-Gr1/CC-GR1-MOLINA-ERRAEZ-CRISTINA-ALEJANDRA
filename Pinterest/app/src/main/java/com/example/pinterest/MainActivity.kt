package com.example.pinterest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieLogo)
        lottieAnimationView.setAnimation(R.raw.pinterest)
        lottieAnimationView.playAnimation()

        // Usar un Handler para retrasar la ejecución del Intent
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@MainActivity, Inicio3::class.java)
            startActivity(intent)
            finish() // Opcional: cerrar la actividad actual
        }, 5000) // 5000 milisegundos = 5 segundos
    }
}
