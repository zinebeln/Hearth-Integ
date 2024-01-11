package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.example.myapplication.model.ViewModel.SharedViewModel
import kotlinx.coroutines.launch

class CardDetailsFragment : Fragment()  {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val decksViewModel: DecksViewModel by activityViewModels()

    private lateinit var textViewCardName: TextView
    private lateinit var textViewCardType: TextView
    private lateinit var imageViewCard: ImageView
//    private lateinit var btn : ImageButton
    private lateinit var btnFav : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CardDetail", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_card_details, container, false)

        textViewCardName = view.findViewById(R.id.textViewCardNameDetails)
        textViewCardType = view.findViewById(R.id.textViewCardTypeDetails)
        imageViewCard = view.findViewById(R.id.imageViewCardDetails)
//        btn = view.findViewById(R.id.imageButtonStar)
        btnFav = view.findViewById(R.id.btnAddToFavorites)


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedCard?.observe(viewLifecycleOwner, Observer { selectedCard ->
            Log.d("CardDetailsFragment", "Selected Card: $selectedCard")

                selectedCard?.let {
                    textViewCardName.text = it.name
                    textViewCardType.text = it.type

                    if (it.img != null) {
                        Glide.with(this)
                            .load(it.img)
                            .into(imageViewCard)
                    } else {
                        imageViewCard.setImageResource(R.drawable.cardv_background)
                    }

//                    btn.setOnClickListener {
//                        lifecycleScope.launch {
//                            Log.d("on click favorite", "click $selectedCard")
//                            decksViewModel.toggleFavoriteStatus(selectedCard)
//                        }
//                    }

                    btnFav.setOnClickListener {
                        lifecycleScope.launch {
                            Log.d("on click favorite", "click $selectedCard")
                            decksViewModel.toggleFavoriteStatus(selectedCard)
                        }
                    }

//                    decksViewModel.isCardFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
//                        updateFavoriteButtonState(isFavorite)
//                    })


                }

        })

        val test =  sharedViewModel.selectedCard?.value
        Log.d("CardDetailselectedcard", "otest $test")
    }

    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        val button = view?.findViewById<Button>(R.id.btnAddToFavorites)

        // Modifiez la couleur du bouton en fonction de l'état
        val colorResId = if (isFavorite) R.color.colorFavorite else R.color.colorNotFavorite
        button?.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))

        // Mettez à jour le texte ou l'icône du bouton en fonction de l'état
        val buttonText = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris"
        button?.text = buttonText
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        textViewCardName = view.findViewById(R.id.textViewCardNameDetails)
//        textViewCardType = view.findViewById(R.id.textViewCardTypeDetails)
//        imageViewCard = view.findViewById(R.id.imageViewCardDetails)
//        btn = view.findViewById(R.id.imageButtonStar)
//
//        val cardId = arguments?.getString("cardId")
//
//        sharedViewModel.selectedCard.observe(viewLifecycleOwner, Observer { card ->
//            Log.d("CardDetailsFragment", "Selected Card: $card")
//
//            // Utilisez les données de la carte pour mettre à jour votre UI
//            card?.let {
//                textViewCardName.text = it.name
//                textViewCardType.text = it.type
//
//                // Chargez l'image avec Glide ou toute autre bibliothèque d'image
//                if (it.img != null) {
//                    Glide.with(this)
//                        .load(it.img)
//                        .into(imageViewCard)
//                } else {
//                    // Utilisez une image par défaut si img est null
//                    imageViewCard.setImageResource(R.drawable.cardv_background)
//                }
//            }
//        })
//    }

}