package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.DecksCardRepository
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard
import kotlinx.coroutines.launch

class DecksViewModel (private val decksRepository: DecksCardRepository) : ViewModel() {


    @Suppress("unused")
    constructor() : this(DecksCardRepository()) {
    }



    private val _decksCards = decksRepository.getAllDecksCards()
    private val _d = MutableLiveData<List<DecksCard>?>()
    val d : LiveData<List<DecksCard>?> get() = _d
    val decksCards: LiveData<List<DecksCard>> get() = _decksCards

    // LiveData pour l'état du favori de la carte actuelle
    private val _isCardFavorite = MutableLiveData<Boolean>()
    val isCardFavorite: LiveData<Boolean> get() = _isCardFavorite

    private val _cardDeletedEvent = MutableLiveData<Unit>()
    val cardDeletedEvent: LiveData<Unit> get() = _cardDeletedEvent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage



    fun delete(decksCard: DecksCard) {
        viewModelScope.launch {
            decksRepository.deleteDeckCard(decksCard)
            _cardDeletedEvent.value = Unit
        }
    }

    fun refreshDeckCards() {
        viewModelScope.launch {
            // Récupérez la nouvelle liste des cartes depuis le référentiel
            val updatedDeckCards = decksRepository.getAllDecksCards()

            _d.postValue(updatedDeckCards.value)
        }
    }
    suspend fun toggleFavoriteStatus(card: Card) {
        Log.d("togglefavorite", "click $card")
        // Vérifiez si la carte est déjà en favori
        val isAlreadyFavorite = decksRepository.getIdDeckCard(card.cardId) != null
        Log.d("isAlreadyFavorite", "click $isAlreadyFavorite")
        card.isFavorite = !card.isFavorite

        // Mettez à jour la LiveData indiquant si la carte est en favori ou non
        _isCardFavorite.value = card.isFavorite

        if (card.isFavorite) {
            if(!isAlreadyFavorite) {
                // Ajouter la carte aux favoris
                Log.d("insert fav", "click $card")
                decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, card = card))
            }else{
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            // Supprimer la carte des favoris
//            decksRepository.deleteDeckCard(DecksCard(cardId = card.cardId, card = card))
            Log.d("dekete fav", "click $card")
            decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, card = card))
           //decksRepository.deleteDeckCard(DecksCard(cardId = card.cardId, card = card))

        }
    }
}