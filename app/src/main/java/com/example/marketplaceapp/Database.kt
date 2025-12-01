package com.example.marketplaceapp
import com.example.marketplaceapp.ui.dashboard.ListingData
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "MyDatabase.db"
        private const val DB_VERSION = 7

        private const val TABLE_LISTINGS = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ITEM = "item"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"

        private const val TABLE_AUTH = "UsersAuth"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        private const val COLUMN_IMAGE = "imageUri"

        private const val COLUMN_USER_ID = "userId"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_LISTINGS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID TEXT,
                $COLUMN_NAME TEXT,
                $COLUMN_ITEM TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_PRICE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IMAGE TEXT
            )
        """.trimIndent()
        db?.execSQL(createTable)

        val createAuthTable = """
            CREATE TABLE $TABLE_AUTH (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT
            )
        """.trimIndent()
        db?.execSQL(createAuthTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LISTINGS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_AUTH")
        onCreate(db)
    }

    // function to insert user data
    fun insertUser(
        userId: String,
        name: String,
        item: String,
        address: String,
        price: String,
        description: String,
        imageUri: String?
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_NAME, name)
            put(COLUMN_ITEM, item)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_IMAGE, imageUri)
        }
        val result = db.insert(TABLE_LISTINGS, null, values)
        db.close()
        return result != -1L
    }

    // read all listings from db
    fun getAllUsers(): List<ListingData> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_LISTINGS", null)
        val userList = mutableListOf<ListingData>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)) ?: ""
                val item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM)) ?: ""
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)) ?: ""
                val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)) ?: ""
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)) ?: ""
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))

                userList.add(
                    ListingData(id, name, item, address, price, desc, imageUri, userId)
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userList
    }

    fun getListingsByUser(userId: String): List<ListingData> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_LISTINGS WHERE $COLUMN_USER_ID = ?",
            arrayOf(userId)
        )

        val list = mutableListOf<ListingData>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))

                list.add(
                    ListingData(id, name, item, address, price, desc, imageUri, userId)
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }


    fun clearAllUsers() {
        val db = writableDatabase
        db.delete(TABLE_LISTINGS, null, null)
    }

    fun deleteListingById(id: Int): Boolean {
        val db = writableDatabase
        val deletedRows = db.delete(
            TABLE_LISTINGS,
            "$COLUMN_ID=?",
            arrayOf(id.toString())
        )
        db.close()
        return deletedRows > 0
    }


    // function to update user's price and desc
    fun updateListingById(
        id: Int,
        newItem: String,
        newAddress: String,
        newPrice: String,
        newDescription: String,
        newImageUri: String?
    ): Boolean {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ITEM, newItem)
            put(COLUMN_ADDRESS, newAddress)
            put(COLUMN_PRICE, newPrice)
            put(COLUMN_DESCRIPTION, newDescription)
            put(COLUMN_IMAGE, newImageUri)
        }

        val result = db.update(
            TABLE_LISTINGS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )

        db.close()
        return result > 0
    }


    fun registerUser(email: String, password: String): Boolean {
        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_AUTH WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )

        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return false
        }

        cursor.close()

        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }

        val result = db.insert(TABLE_AUTH, null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_AUTH WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password)
        )

        val exists = cursor.moveToFirst()

        cursor.close()
        db.close()
        return exists
    }
}
