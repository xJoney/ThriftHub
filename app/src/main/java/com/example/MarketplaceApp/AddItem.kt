package com.example.MarketplaceApp


import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)



        dbHelper = DatabaseHelper(this)
        dbHelper.readableDatabase

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val itemInput = findViewById<EditText>(R.id.itemInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val priceInput = findViewById<EditText>(R.id.priceInput)
        val descInput = findViewById<EditText>(R.id.descInput)

        val addButton = findViewById<Button>(R.id.addButton)
        val outputText = findViewById<TextView>(R.id.outputText)
        val clear = findViewById<Button>(R.id.clear)

        val users = dbHelper.getAllUsers()
        outputText.text = users.joinToString("\n")

        // takes users input and insert into DB
        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val item = itemInput.text.toString()
            val address = addressInput.text.toString()
            val price = priceInput.text.toString()
            val description = descInput.text.toString()

            if (dbHelper.insertUser(name, item, address, price, description)) {
                Toast.makeText(this, "User added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show()
            }
            val users = dbHelper.getAllUsers()
            outputText.text = users.joinToString("\n")
        }

        // clears all users
        clear.setOnClickListener {
            dbHelper.clearAllUsers()
            val users = dbHelper.getAllUsers()
            outputText.text = users.joinToString("\n")
            Toast.makeText(this, "All records deleted", Toast.LENGTH_SHORT).show()
        }

        // back button to go back to home page
        val back = findViewById<Button>(R.id.backbtn)
        back.setOnClickListener {
            finish()
        }
    }
}
