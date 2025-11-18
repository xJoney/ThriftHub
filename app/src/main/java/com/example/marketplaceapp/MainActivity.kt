package com.example.marketplaceapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        // hide bottom nav until login
        bottomNavView.visibility = View.GONE

        // control visibility based on navigation + auth
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // pages where bottom nav MUST NOT show
                R.id.LoginFragment,
                R.id.SignupFragment -> {
                    bottomNavView.visibility = View.GONE
                }

                else -> {
                    // show only if logged in
                    val user = FirebaseAuth.getInstance().currentUser
                    bottomNavView.visibility = if (user != null) View.VISIBLE else View.GONE
                }
            }
        }
    }
}
