package com.example.proyectomovilesb2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.app.Activity
import android.content.Intent

class EditStoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_store)

        val storeName = intent.getStringExtra("store_name")
        val storePosition = intent.getIntExtra("store_position", -1)

        val editText = findViewById<EditText>(R.id.et_store_name)
        editText.setText(storeName)

        findViewById<Button>(R.id.btn_save_store).setOnClickListener {
            val newStoreName = editText.text.toString()
            val resultIntent = Intent().apply {
                putExtra("new_store_name", newStoreName)
                putExtra("store_position", storePosition)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}