package com.example.MarketplaceApp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // read from database
        dbHelper = DatabaseHelper(this)
        dbHelper.readableDatabase

        val db = findViewById<TextView>(R.id.outputText)
        val users = dbHelper.getAllUsers()
        db.text = users.joinToString("\n")

        val addItemPage = findViewById<Button>(R.id.addItem)


        // button to go to add item page
        addItemPage.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }
}
