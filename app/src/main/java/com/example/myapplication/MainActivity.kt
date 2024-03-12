package com.example.myapplication


import android.annotation.SuppressLint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.ui.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dataBase.AppDatabase


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.userFragment, R.id.cardFragment, R.id.section2Fragment, R.id.section3Fragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
  }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
       // return super.onSupportNavigateUp()
    }
}




