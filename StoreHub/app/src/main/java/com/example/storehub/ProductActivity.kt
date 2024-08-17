package com.example.storehub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.activity.result.contract.ActivityResultContracts


class ProductActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private val products = mutableListOf<String>()
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var storeName: String

    private val editProductLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val position = data?.getIntExtra("product_position", -1)
            val newName = data?.getStringExtra("new_product_name")
            if (position != null && position != -1 && newName != null) {
                val oldName = products[position]
                dbHelper.updateProduct(storeName, oldName, newName)
                products[position] = newName
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        dbHelper = DBHelper(this)
        storeName = intent.getStringExtra("store_name") ?: ""
        findViewById<TextView>(R.id.tv_store_name).text = storeName

        listView = findViewById(R.id.list_products)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, products)
        listView.adapter = adapter

        findViewById<Button>(R.id.btn_create_product).setOnClickListener {
            createNewProduct()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            showProductOptions(position)
        }

        loadProducts()
    }

    private fun createNewProduct() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crear nuevo producto")

        val input = EditText(this)
        input.hint = "Nombre del producto"
        builder.setView(input)

        builder.setPositiveButton("Crear") { _, _ ->
            val productName = input.text.toString()
            if (productName.isNotEmpty()) {
                dbHelper.addProduct(storeName, productName)
                products.add(productName)
                adapter.notifyDataSetChanged()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun showProductOptions(position: Int) {
        val productName = products[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle(productName)
        builder.setItems(arrayOf("Editar", "Eliminar")) { _, which ->
            when (which) {
                0 -> { // Editar
                    val intent = Intent(this, EditProductActivity::class.java).apply {
                        putExtra("product_name", productName)
                        putExtra("product_position", position)
                    }
                    editProductLauncher.launch(intent)
                }
                1 -> { // Eliminar
                    dbHelper.deleteProduct(storeName, productName)
                    products.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        builder.show()
    }

    private fun loadProducts() {
        products.clear()
        products.addAll(dbHelper.getProducts(storeName))
        adapter.notifyDataSetChanged()
    }
}