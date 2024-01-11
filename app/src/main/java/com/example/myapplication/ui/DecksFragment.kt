package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.myapplication.model.DecksCard
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DecksFragment : Fragment()  {
   // private val deckViewModel: DecksViewModel by activityViewModels()
    private val deckViewModel: DecksViewModel by viewModels()
//    private val decksAdapter = DecksAdapter()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_decks, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.decksRecyclerView)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationDecks)
       // decksAdapter = DecksAdapter(emptyList())
       // recyclerView.adapter = decksAdapter

        val decksAdapter = DecksAdapter(object : DecksAdapter.OnItemClickListener {
            override fun onItemClicked(decksCard: DecksCard) {
                showConfirmationDialog(decksCard)
            }
        })

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

        deckViewModel.cardDeletedEvent.observe(viewLifecycleOwner, Observer {
            // Mettez à jour la liste des cartes ou effectuez d'autres actions nécessaires
            deckViewModel.refreshDeckCards() // Assurez-vous d'avoir une fonction pour rafraîchir vos données
        })

        deckViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessageResId ->
            // Utilisez la ressource de chaîne pour afficher le message d'erreur
            val errorMessage = getString(errorMessageResId.toInt())
            Toast.makeText(requireContext(), "Erreur : doublons carte favoris", Toast.LENGTH_SHORT)
            // Affichez le message d'erreur, par exemple, dans un Toast ou à l'aide d'une vue TextView
        })


        val navController = findNavController()
        // Attachez le NavController au BottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        return view
    }

    private fun showConfirmationDialog(decksCard: DecksCard) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Voulez-vous supprimer cette carte de vos favoris?")
        builder.setPositiveButton("Oui") { _, _ ->
            // Supprimez la carte de vos favoris ici en appelant votre méthode de suppression.
            deckViewModel.delete(decksCard)
            Toast.makeText(requireContext(), "Carte supprimée des favoris", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss() }

        builder.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Utilisez le ViewModel pour observer les changements dans la liste des favoris

    }

}