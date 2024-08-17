package com.example.storehub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.content.Intent

class EditStoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_store)

        val storeName = intent.getStringExtra("store_name")
        val storePosition = intent.getIntExtra("store_position", -1)
        val storeLat = intent.getStringExtra("store_lat") // Clave correcta para la latitud
        val storeLng = intent.getStringExtra("store_lon") // Clave correcta para la longitud

        val nameEditText = findViewById<EditText>(R.id.et_store_name)
        val latEditText = findViewById<EditText>(R.id.editLatitud)
        val lonEditText = findViewById<EditText>(R.id.editLongitud)

        nameEditText.setText(storeName)
        latEditText.setText(storeLat)
        lonEditText.setText(storeLng)

        findViewById<Button>(R.id.btn_save_store).setOnClickListener {
            val newStoreName = nameEditText.text.toString()
            val newLat = latEditText.text.toString()
            val newLng = lonEditText.text.toString()

            val resultIntent = Intent().apply {
                putExtra("new_store_name", newStoreName)
                putExtra("store_position", storePosition)
                putExtra("new_lat", newLat)
                putExtra("new_lon", newLng)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
