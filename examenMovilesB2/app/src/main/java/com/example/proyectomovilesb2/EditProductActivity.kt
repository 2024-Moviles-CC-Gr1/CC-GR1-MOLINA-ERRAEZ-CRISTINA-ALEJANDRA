package com.example.proyectomovilesb2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.app.Activity
import android.content.Intent


class EditProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val productName = intent.getStringExtra("product_name")
        val productPosition = intent.getIntExtra("product_position", -1)

        val editText = findViewById<EditText>(R.id.et_product_name)
        editText.setText(productName)

        findViewById<Button>(R.id.btn_save_product).setOnClickListener {
            val newProductName = editText.text.toString()
            val resultIntent = Intent().apply {
                putExtra("new_product_name", newProductName)
                putExtra("product_position", productPosition)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
