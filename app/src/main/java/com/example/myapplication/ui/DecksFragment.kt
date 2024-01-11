package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DecksFragment : Fragment()  {
   // private val deckViewModel: DecksViewModel by activityViewModels()
    private val deckViewModel: DecksViewModel by viewModels()
    private val decksAdapter = DecksAdapter()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_decks, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.decksRecyclerView)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationDecks)
       // decksAdapter = DecksAdapter(emptyList())
        recyclerView.adapter = decksAdapter

        // Ajoutez cette ligne pour définir un LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2 colonnes, ajustez selon vos besoins
        }

        // ...
        deckViewModel.decksCards.observe(viewLifecycleOwner, Observer { decksCards ->

            decksAdapter.submitList(decksCards)
        })

        val navController = findNavController()
        // Attachez le NavController au BottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Utilisez le ViewModel pour observer les changements dans la liste des favoris

    }

}