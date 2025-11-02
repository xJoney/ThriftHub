package com.example.marketplaceapp


import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

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

        val users = dbHelper.getAllUsers()

        //add button - waits for user to click add, inserts user inputs into db
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
//        val clear = findViewById<Button>(R.id.clear)
//        clear.setOnClickListener {
//            dbHelper.clearAllUsers()
//            val users = dbHelper.getAllUsers()
//            outputText.text = users.joinToString("\n")
//            Toast.makeText(this, "All records deleted", Toast.LENGTH_SHORT).show()
//        }

        val delete = findViewById<Button>(R.id.deletebtn)
        delete.setOnClickListener {
            val item = itemInput.text.toString()
            if (dbHelper.delUser(item)) {
                Toast.makeText(this, "Deleted item!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }

        // update button - find the row with name and updates new price and description from user input
        val update = findViewById<Button>(R.id.updatebtn)
        update.setOnClickListener {
            val name = nameInput.text.toString()
            val price = priceInput.text.toString()
            val description = descInput.text.toString()
            if (dbHelper.updateUser(name,price,description)) {
                Toast.makeText(this, "Updated user!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show()
            }
            val users = dbHelper.getAllUsers()
            outputText.text = users.joinToString("\n")
        }

        //back button to return to homepage
        val back = findViewById<Button>(R.id.backbtn)
        back.setOnClickListener {
            finish()
        }
    }
}
