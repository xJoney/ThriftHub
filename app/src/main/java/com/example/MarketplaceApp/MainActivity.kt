package com.example.MarketplaceApp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MarketplaceApp.UserAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter


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


        // recycler view to layout in grid format
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        recyclerView.adapter = UserAdapter(users)

        // button to go to add item page
        val addItemPage = findViewById<Button>(R.id.addItem)
        addItemPage.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }


    }

    // reloads the data from the database to get updated ver.
    override fun onResume() {
        super.onResume()
        val updatedUsers = dbHelper.getAllUsers()
        adapter = UserAdapter(updatedUsers)
        recyclerView.adapter = adapter
    }
}
