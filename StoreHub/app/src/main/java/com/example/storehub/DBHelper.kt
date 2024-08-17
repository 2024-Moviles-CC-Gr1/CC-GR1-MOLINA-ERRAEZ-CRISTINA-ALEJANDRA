package com.example.storehub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "store_products.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_STORE_NAME = "store_name"
        const val COLUMN_PRODUCT_NAME = "product_name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_PRODUCTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_STORE_NAME TEXT,"
                + "$COLUMN_PRODUCT_NAME TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(storeName: String, productName: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_STORE_NAME, storeName)
        values.put(COLUMN_PRODUCT_NAME, productName)
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    fun getProducts(storeName: String): List<String> {
        val productList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            arrayOf(COLUMN_PRODUCT_NAME),
            "$COLUMN_STORE_NAME=?",
            arrayOf(storeName),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                productList.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    fun updateProduct(storeName: String, oldProductName: String, newProductName: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PRODUCT_NAME, newProductName)
        db.update(
            TABLE_PRODUCTS,
            values,
            "$COLUMN_STORE_NAME=? AND $COLUMN_PRODUCT_NAME=?",
            arrayOf(storeName, oldProductName)
        )
        db.close()
    }

    fun deleteProduct(storeName: String, productName: String) {
        val db = this.writableDatabase
        db.delete(
            TABLE_PRODUCTS,
            "$COLUMN_STORE_NAME=? AND $COLUMN_PRODUCT_NAME=?",
            arrayOf(storeName, productName)
        )
        db.close()
    }
}