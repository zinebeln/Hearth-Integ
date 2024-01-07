package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.ui.UserFragment



class MainActivity : AppCompatActivity() {

    private lateinit var infoViewModelCard: CardViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

//        val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)
//        val buttonOpenFragment = findViewById<Button>(R.id.buttonOpenFragment)

        setupActionBarWithNavController(navController, appBarConfiguration)

//        Log.d("MainActivity", "Avant la transaction de fragment")
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, UserFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//        Log.d("MainActivity", "Après la transaction de fragment")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}




