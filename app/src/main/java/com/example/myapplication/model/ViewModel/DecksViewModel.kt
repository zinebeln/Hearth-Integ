package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.DecksCardRepository
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard

class DecksViewModel (private val decksRepository: DecksCardRepository) : ViewModel() {


    @Suppress("unused")
    constructor() : this(DecksCardRepository()) {
    }



    private val _decksCards = decksRepository.getAllDecksCards()
    val decksCards: LiveData<List<DecksCard>> get() = _decksCards

    suspend fun toggleFavoriteStatus(card: Card) {
        Log.d("togglefavorite", "click $card")
        card.isFavorite = !card.isFavorite

        if (card.isFavorite) {
            // Ajouter la carte aux favoris
            decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, card = card))
        } else {
            // Supprimer la carte des favoris
            decksRepository.deleteDecksCard(DecksCard(cardId = card.cardId, card = card))
        }
    }
}