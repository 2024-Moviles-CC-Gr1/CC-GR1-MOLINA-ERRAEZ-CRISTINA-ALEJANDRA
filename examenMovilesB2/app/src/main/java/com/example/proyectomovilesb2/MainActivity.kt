package com.example.proyectomovilesb2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private val stores = mutableListOf("Tienda 1", "Tienda 2")
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val editStoreLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val position = data?.getIntExtra("store_position", -1)
            val newName = data?.getStringExtra("new_store_name")
            if (position != null && position != -1 && newName != null) {
                stores[position] = newName
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        listView = findViewById(R.id.lista_tiendas)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stores)
        listView.adapter = adapter

        findViewById<Button>(R.id.btn_create_tienda).setOnClickListener {
            createNewStore()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            showStoreOptions(position)
        }
    }

    private fun createNewStore() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crear nueva tienda")

        val input = EditText(this)
        input.hint = "Nombre de la tienda"
        builder.setView(input)

        builder.setPositiveButton("Crear") { _, _ ->
            val storeName = input.text.toString()
            if (storeName.isNotEmpty()) {
                stores.add(storeName)
                adapter.notifyDataSetChanged()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun showStoreOptions(position: Int) {
        val storeName = stores[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle(storeName)
        builder.setItems(arrayOf("Editar", "Eliminar", "Ver Productos")) { _, which ->
            when (which) {
                0 -> { // Editar
                    val intent = Intent(this, EditStoreActivity::class.java).apply {
                        putExtra("store_name", storeName)
                        putExtra("store_position", position)
                    }
                    editStoreLauncher.launch(intent)
                }
                1 -> { // Eliminar
                    stores.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                2 -> { // Ver Productos
                    val intent = Intent(this, ProductActivity::class.java).apply {
                        putExtra("store_name", storeName)
                    }
                    startActivity(intent)
                }
            }
        }
        builder.show()
    }
}