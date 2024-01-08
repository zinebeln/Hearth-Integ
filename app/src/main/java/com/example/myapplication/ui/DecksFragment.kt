package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DecksFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_decks, container, false)


        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationDecks)
        val navController = findNavController()
        // Attachez le NavController au BottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//
//                R.id.action_cards -> {
//                    navController.navigate(R.id.action_deckFragment_to_cardFragment)
//                    true
//                }
//                R.id.action_decks -> {
//                   // navController.navigate(R.id.action_cardFragment_to_section2Fragment)
//                    true
//                }
//
//                R.id.action_profil -> {
//                    navController.navigate(R.id.action_deckFragment_to_profilFragment)
//                    true
//                }
//
//                else -> false
//            }
//        }

        return view
    }

}