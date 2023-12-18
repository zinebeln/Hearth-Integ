package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup


import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.model.ViewModel.InfoViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.factory.AllCardViewModel
import com.example.myapplication.ui.CardFragment
import com.example.myapplication.ui.UserFragment
import dataBase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var infoViewModelCard: CardViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Configure le composant de navigation
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)
        val buttonOpenFragment = findViewById<Button>(R.id.buttonOpenFragment)

        // Configure la barre d'action avec le composant de navigation
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserFragment())
                .commit()
        }
        buttonOpenFragment.setOnClickListener {
            // Remplacez le contenu du conteneur par votre fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CardFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}




