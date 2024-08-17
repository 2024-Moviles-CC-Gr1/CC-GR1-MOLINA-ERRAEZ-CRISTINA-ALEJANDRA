package com.example.storehub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Store(var id: Long = -1, val name: String, val lat: String, val lng: String)

class StoreDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "store_manager.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_STORES = "stores"
        const val COLUMN_ID = "id"
        const val COLUMN_STORE_NAME = "store_name"
        const val COLUMN_STORE_LAT = "latitud"
        const val COLUMN_STORE_LNG = "longitud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_STORES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_STORE_NAME TEXT,"
                + "$COLUMN_STORE_LAT REAL,"
                + "$COLUMN_STORE_LNG REAL)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STORES")
        onCreate(db)
    }

    fun addStore(store: Store) {
        val db = this.writableDatabase
        try {
            val values = ContentValues().apply {
                put(COLUMN_STORE_NAME, store.name)
                put(COLUMN_STORE_LAT, store.lat.toDoubleOrNull())
                put(COLUMN_STORE_LNG, store.lng.toDoubleOrNull())
            }
            val id = db.insert(TABLE_STORES, null, values)
            store.id = id // Actualizar el ID del store con el valor insertado
        } catch (e: Exception) {
            e.printStackTrace() // Manejo básico de excepciones
        } finally {
            db.close()
        }
    }

    fun updateStore(store: Store) {
        val db = this.writableDatabase
        try {
            val values = ContentValues().apply {
                put(COLUMN_STORE_NAME, store.name)
                put(COLUMN_STORE_LAT, store.lat.toDoubleOrNull())
                put(COLUMN_STORE_LNG, store.lng.toDoubleOrNull())
            }
            db.update(
                TABLE_STORES,
                values,
                "$COLUMN_ID=?",
                arrayOf(store.id.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace() // Manejo básico de excepciones
        } finally {
            db.close()
        }
    }

    fun deleteStore(store: Store) {
        val db = this.writableDatabase
        try {
            db.delete(
                TABLE_STORES,
                "$COLUMN_ID=?",
                arrayOf(store.id.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace() // Manejo básico de excepciones
        } finally {
            db.close()
        }
    }

    fun getStores(): List<Store> {
        val storeList = mutableListOf<Store>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_STORES,
            arrayOf(COLUMN_ID, COLUMN_STORE_NAME, COLUMN_STORE_LAT, COLUMN_STORE_LNG),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val storeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_NAME))
                val lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_STORE_LAT)).toString()
                val lng = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_STORE_LNG)).toString()
                storeList.add(Store(id, storeName, lat, lng))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return storeList
    }
}
