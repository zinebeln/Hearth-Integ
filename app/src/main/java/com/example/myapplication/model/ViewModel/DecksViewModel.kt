package com.example.myapplication.model.ViewModel

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.DecksCardRepository
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard
import com.example.myapplication.ui.AuthManager
import kotlinx.coroutines.launch

class DecksViewModel (private val decksRepository: DecksCardRepository, private val userRepository: UserRepository ) : ViewModel() {
    @Suppress("unused")
    constructor() : this(DecksCardRepository(), UserRepository() ) {
    }

    private val _isCardFavorite = MutableLiveData<Boolean>()
    val isCardFavorite: LiveData<Boolean> get() = _isCardFavorite

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _cardDeletedEvent = MutableLiveData<Unit>()
    val cardDeletedEvent: LiveData<Unit> get() = _cardDeletedEvent

    private val _favoriteCards = MutableLiveData<List<DecksCard>>()
    val favoriteCards: LiveData<List<DecksCard>> = _favoriteCards

    private lateinit var auth: AuthManager
    private lateinit var list : List<DecksCard>

    fun init(auth: AuthManager) {
        this.auth = auth
    }

    fun getFavoriteCardsForUser(username: String) {
        viewModelScope.launch {
            val userId = userRepository.getUserId(username)
            if (userId != null) {
                val favoriteCardsForUser = decksRepository.decksCardFav(userId)
                _favoriteCards.postValue(favoriteCardsForUser)
            } else {
                Log.d("erreur fav", "utilisateur n'existe pas")
            }
        }
    }

    fun delete(decksCard: DecksCard) {
        viewModelScope.launch {
            decksRepository.deleteDeckCard(decksCard)
            _cardDeletedEvent.value = Unit
        }
    }
//    fun getFavCardID() : List<DecksCard> {
//
//        viewModelScope.launch {
//          list =  decksRepository.getFavoriteCardsID()
//        }
//
//        return list ;
//    }

    fun deleteCardFromDeck(userId: Long, cardId: String) {

        viewModelScope.launch {
            // Appeler la fonction de suppression dans le repository
            decksRepository.removeFromDecks(userId, cardId)
            // Vous pouvez également mettre à jour LiveData ici pour refléter la suppression
        }
    }

    fun refreshDeckCards3() {
        viewModelScope.launch {
            val updatedFavoriteCards = decksRepository.getFavoriteCards()
            _favoriteCards.value = updatedFavoriteCards
        }
    }

    suspend fun toggleFavoriteStatus3(card: Card, userId: Long?) {

        val isAlreadyFavorite = decksRepository.decksCardFav(userId ?: 0)?.any { it.cardsId == card.cardId } ?: false
        card.isFavorite = !isAlreadyFavorite

        _isCardFavorite.value = card.isFavorite

        if (card.isFavorite) {
            if (!isAlreadyFavorite) {
                decksRepository.addToFavorites(userId ?: 0, card.cardId, card)

            } else {
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            Log.d("delete fav", "click $card")
            // Supprimer la carte des favoris
        }
    }

    suspend fun toggleFavoriteStatus4(card: Card, userId: Long?) {

        val isAlreadyFavorite = decksRepository.decksCardFav(userId ?: 0)?.any { it.cardsId == card.cardId } ?: false

        if (isAlreadyFavorite && !card.isFavorite) {
            // decksRepository.removeFromFavorites(userId ?: 0, card.cardId)
            _isCardFavorite.value = false
        } else if (!isAlreadyFavorite && card.isFavorite) {
            // La carte n'est pas favorite, et l'utilisateur veut l'ajouter aux favoris.
            decksRepository.addToFavorites(userId ?: 0, card.cardId, card)
            _isCardFavorite.value = true
        } else {
            // Gestion des cas inattendus ou affichage d'un message approprié.
            Log.d("erreur", "erreur ajout fav")
//            _errorMessage.value = R.string.error_message.toString()
        }

    }

}