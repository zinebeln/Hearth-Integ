package com.example.myapplication.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope


import androidx.lifecycle.viewModelScope

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.DecksCard
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.example.myapplication.model.ViewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DecksFragment : Fragment()  {

    private var previousFavoriteCardsSize = 0
    private var previousFavoriteCards: List<DecksCard> = emptyList()
    private val deckViewModel: DecksViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels ()
    private lateinit var decksAdapter: DecksAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_decks, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.decksRecyclerView)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationDecks)

//        decksAdapter = DecksAdapter(object : DecksAdapter.OnItemClickListener {
//            override fun onItemClicked(decksCard: DecksCard) {
//              //  showConfirmationDialog(decksCard)
//               findNavController().navigate(R.id.action_deckFragment_to_cardDetailFragment)
//            }
//        })

        decksAdapter = DecksAdapter(object : DecksAdapter.OnItemClickListener {
            override fun onItemClicked(decksCard: DecksCard) {
                // Votre logique de clic
                sharedViewModel.selectCardDeck(decksCard)
                findNavController().navigate(R.id.action_deckFragment_to_cardDetailFragment)
            }
        })

//        decksAdapter.setOnItemClickListener(object : DecksAdapter.OnItemClickListener {
//            override fun onItemClicked(card: DecksCard) {
//                sharedViewModel.selectCardDeck(card)
//               findNavController().navigate(R.id.action_cardFragment_to_cardDetailsFragment)
//            }
//        })

        val auth = AuthManager(requireContext())
        deckViewModel.init(auth)
        recyclerView.adapter = decksAdapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")?: ""
        deckViewModel.getFavoriteCardsForUser(username)

        deckViewModel.favoriteCards.observe(viewLifecycleOwner, Observer { favoriteCards ->
            decksAdapter.submitList(favoriteCards)

            if (favoriteCards.size < previousFavoriteCardsSize) {
                val removedFavoriteCard = previousFavoriteCards.last { it !in favoriteCards }
                Toast.makeText(requireContext(), "Carte supprimée des favoris: ${removedFavoriteCard.card.name}", Toast.LENGTH_SHORT).show()
            }
            previousFavoriteCardsSize = favoriteCards.size
            previousFavoriteCards = favoriteCards.toList()
        })

        deckViewModel.cardDeletedEvent.observe(viewLifecycleOwner, Observer {
            deckViewModel.refreshDeckCards3()
        })

        deckViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessageResId ->
         Toast.makeText(requireContext(), "Erreur : doublons carte favoris", Toast.LENGTH_SHORT)
       })

        val navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)
        return view
    }

    private fun showConfirmationDialog(decksCard: DecksCard) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Voulez-vous supprimer cette carte de vos favoris?")
        builder.setPositiveButton("Oui") { _, _ ->
            deckViewModel.delete(decksCard)
            Toast.makeText(requireContext(), "Carte supprimée des favoris", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss() }
        builder.show()
    }

}