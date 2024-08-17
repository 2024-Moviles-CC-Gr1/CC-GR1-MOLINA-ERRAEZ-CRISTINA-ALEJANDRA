package com.example.storehub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: StoreDBHelper
    private val stores = mutableListOf<Store>()
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<Store>

    private val editStoreLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val position = data?.getIntExtra("store_position", -1)
            val newName = data?.getStringExtra("new_store_name")
            val newLat = data?.getStringExtra("new_lat")
            val newLng = data?.getStringExtra("new_lon")
            if (position != null && position != -1 && newName != null && newLat != null && newLng != null) {
                val oldStore = stores[position]
                val updatedStore = Store(oldStore.id, newName, newLat, newLng)
                dbHelper.updateStore(updatedStore)
                stores[position] = updatedStore
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        dbHelper = StoreDBHelper(this)

        listView = findViewById(R.id.lista_tiendas)

        // Usar un ArrayAdapter con el layout personalizado
        adapter = object : ArrayAdapter<Store>(this, R.layout.list_item_store, R.id.store_name, stores) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val store = getItem(position)
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_store, parent, false)
                val storeNameTextView = view.findViewById<TextView>(R.id.store_name)
                storeNameTextView.text = store?.name
                return view
            }
        }
        listView.adapter = adapter

        findViewById<Button>(R.id.btn_create_tienda).setOnClickListener {
            createNewStore()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            showStoreOptions(position)
        }

        loadStores()
    }

    private fun createNewStore() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crear nueva tienda")

        val inputName = EditText(this).apply {
            hint = "Nombre de la tienda"
        }

        val inputLat = EditText(this).apply {
            hint = "Latitud"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL or android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
        }

        val inputLng = EditText(this).apply {
            hint = "Longitud"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL or android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(inputName)
            addView(inputLat)
            addView(inputLng)
        }

        builder.setView(layout)

        builder.setPositiveButton("Crear") { _, _ ->
            val storeName = inputName.text.toString()
            val storeLat = inputLat.text.toString()
            val storeLng = inputLng.text.toString()
            if (storeName.isNotEmpty() && storeLat.isNotEmpty() && storeLng.isNotEmpty()) {
                val newStore = Store(name = storeName, lat = storeLat, lng = storeLng)
                dbHelper.addStore(newStore)
                stores.add(newStore)
                adapter.notifyDataSetChanged()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun showStoreOptions(position: Int) {
        val store = stores[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle(store.name)
        builder.setItems(arrayOf("Editar", "Eliminar", "Ver Productos", "Ver en el Mapa")) { _, which ->
            when (which) {
                0 -> { // Editar
                    val intent = Intent(this, EditStoreActivity::class.java).apply {
                        putExtra("store_name", store.name)
                        putExtra("store_position", position)
                        putExtra("store_lat", store.lat)
                        putExtra("store_lon", store.lng)
                    }
                    editStoreLauncher.launch(intent)
                }
                1 -> { // Eliminar
                    dbHelper.deleteStore(store)
                    stores.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                2 -> { // Ver Productos
                    val intent = Intent(this, ProductActivity::class.java).apply {
                        putExtra("store_name", store.name)
                    }
                    startActivity(intent)
                }
                3 -> { // Ver en el Mapa
                    val intent = Intent(this, MapsActivity::class.java).apply {
                        putExtra("store_name", store.name)
                        putExtra("store_lat", store.lat)
                        putExtra("store_lon", store.lng)
                    }
                    startActivity(intent)
                }
            }
        }
        builder.show()
    }

    private fun loadStores() {
        stores.clear()
        stores.addAll(dbHelper.getStores())
        adapter.notifyDataSetChanged()
    }
}
