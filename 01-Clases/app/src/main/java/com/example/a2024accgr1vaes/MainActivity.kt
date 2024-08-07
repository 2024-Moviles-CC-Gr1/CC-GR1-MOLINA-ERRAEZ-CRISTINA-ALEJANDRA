package com.example.a2024accgr1vaes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    fun mostrarSnackBar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.id_layout_main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    val cakkbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    val data = result.data;
                    mostrarSnackBar(
                        "${data?.getStringExtra("nombreModificado")}"
                    )
                }
            }
        }

    val callbackContenidoIntentImplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    if(result.data != null){
                        val uri: Uri = result.data!!.data!!
                        val cursor = contentResolver.query(
                            uri, null, null, null, null, null
                        )
                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val telefono = cursor?.getString(indiceTelefono!!)
                        cursor?.close()
                        mostrarSnackBar("Telefono $telefono")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonCicloVida
            .setOnClickListener{
                irActividad(ACicloVida::class.java)
            }
        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView
            .setOnClickListener{
                irActividad(BListView::class.java)
            }
        val botonIntentImplicito = findViewById<Button>(
            R.id.btn_ir_intent_implicito
        )
        botonIntentImplicito
            .setOnClickListener {
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                callbackContenidoIntentImplicito.launch(intentConRespuesta)
            }
        val botonInentExplicito = findViewById<Button>(
            R.id.btn_ir_intent_explicito
        )
        botonInentExplicito
            .setOnClickListener{
                val intentExplicito = Intent(
                    this,
                    CIntentExplicitoParametros::class.java
                )
                intentExplicito.putExtra("nombre", "Cristina")
                intentExplicito.putExtra("apellido", "Molina")
                intentExplicito.putExtra("edad", 21)
                intentExplicito.putExtra(
                    "entrenador",
                    BEntrenador(10, "Cristina","Molina")
                )
            }

        EBaseDeDatos.tablaEntrenador = ESqliteHelperEntrenador(
            this
        )
        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite.setOnClickListener{
            irActividad(ECrudEntrenador::class.java)
        }
    }


    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}