package com.example.MarketplaceApp

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "MyDatabase.db"
        private const val DB_VERSION = 4
        private const val COLUMN_NAME = "name"
        private const val TABLE_NAME = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_ITEM = "item"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_ITEM TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_PRICE TEXT,
                $COLUMN_DESCRIPTION TEXT
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    // insert data into db
    fun insertUser(name: String, item: String, address: String, price: String, description: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_ITEM, item)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)


        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }


    //read all users
    fun getAllUsers(): List<User> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val userList = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val user = User(name, item, address, price, desc)
                userList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return userList
    }

    //clear all users in the db --> change to erasing one item at a time
    fun clearAllUsers() {
        val db = writableDatabase
        db.delete("Users", null, null)
        // Don’t call db.close() — keep it open for inspection
    }

    // delete func
    fun delUser(){
        val db = writableDatabase
        val deletedRows = db.delete("Users", null, null)
    }

    // update func
    fun updateUser(){
        //add update
    }
    }