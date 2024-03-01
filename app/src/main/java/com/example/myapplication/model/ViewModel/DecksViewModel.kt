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

    private val _favoriteCards = MutableLiveData<List<DecksCard>>()
    val favoriteCards: LiveData<List<DecksCard>> = _favoriteCards

    private lateinit var auth: AuthManager

    fun init(auth: AuthManager) {
        this.auth = auth // Initialisation de la propriété auth
    }

    private val userIdObserver = Observer<Long> { userId ->
      
    }

    // Méthode pour récupérer les cartes favorites d'un utilisateur
    fun getFavoriteCardsForUser(userId: Long) {
        viewModelScope.launch {
            val favoriteCardsForUser = decksRepository.decksCardFav(userId)
            _favoriteCards.postValue(favoriteCardsForUser)
        }
    }

    fun getFavoriteCardsForUser(username: String) {
        viewModelScope.launch {
            val userId = userRepository.getUserId(username)
            if (userId != null) {
                val favoriteCardsForUser = decksRepository.decksCardFav(userId)
                _favoriteCards.postValue(favoriteCardsForUser)
            } else {
                // Gérer le cas où l'utilisateur n'existe pas
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
    fun deleteFavoriteCard(decksCard: DecksCard) {
        viewModelScope.launch {
            decksRepository.deleteDeckCard(decksCard)
        }
    }

    fun refreshDeckCards() {
        viewModelScope.launch {
            // Récupérez la nouvelle liste des cartes depuis le référentiel
            val updatedDeckCards = decksRepository.getAllDecksCards()

            _d.postValue(updatedDeckCards.value)
        }
    }

    fun refreshDeckCards2() {
        viewModelScope.launch {
            // Récupérez la nouvelle liste des cartes depuis le référentiel
            val nullableLong: Long? = auth.user.value?.userId
            val nonNullableLong: Long = nullableLong ?: 0L // Utilisez une valeur par défaut, par exemple 0L

            val updatedDeckCards = decksRepository.getAllDecksCards2(nonNullableLong)

            _d.postValue(updatedDeckCards.value)
        }
    }

    fun refreshDeckCards3() {
        viewModelScope.launch {
            // Récupérer la liste mise à jour des cartes favorites après la suppression
            val updatedFavoriteCards = decksRepository.getFavoriteCards()
            _favoriteCards.value = updatedFavoriteCards
        }
    }
    suspend fun toggleFavoriteStatus(card: Card) {
        Log.d("togglefavorite", "click $card")
        val isAlreadyFavorite = decksRepository.getIdDeckCard(card.cardId) != null
        Log.d("isAlreadyFavorite", "click $isAlreadyFavorite")
        card.isFavorite = !card.isFavorite
        _isCardFavorite.value = card.isFavorite

        if (card.isFavorite) {
            if(!isAlreadyFavorite) {
                // Ajouter la carte aux favoris
                Log.d("insert fav", "click $card")
            }else{
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            Log.d("dekete fav", "click $card")
        }
    }


    suspend fun toggleFavoriteStatus2(card: Card, userId : Long?) {
        Log.d("togglefavorite", "click $card")
        val isAlreadyFavorite = decksRepository.getIdDeckCard(card.cardId) != null
        Log.d("isAlreadyFavorite", "click $isAlreadyFavorite")
        card.isFavorite = !card.isFavorite

        // Mettez à jour la LiveData indiquant si la carte est en favori ou non
        _isCardFavorite.value = card.isFavorite

        if (card.isFavorite) {
            if(!isAlreadyFavorite) {
                // Ajouter la carte aux favoris
                Log.d("insert fav", "click $card")
                decksRepository.insertDecksCard2(DecksCard(cardsId = card.cardId, card = card, userId = userId ))
            }else{
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            Log.d("dekete fav", "click $card")
            Log.d("user id $userId ", "test");
            decksRepository.insertDecksCard2(DecksCard(cardsId = card.cardId, card = card, userId = userId ))

        }
    }

    suspend fun toggleFavoriteStatus3(card: Card, userId: Long?) {
        Log.d("togglefavorite", "click $card")
        val isAlreadyFavorite = decksRepository.decksCardFav(userId ?: 0)?.any { it.cardsId == card.cardId } ?: false
        Log.d("isAlreadyFavorite", "click $isAlreadyFavorite")
        card.isFavorite = !isAlreadyFavorite

        // Mettez à jour la LiveData indiquant si la carte est en favori ou non
        _isCardFavorite.value = card.isFavorite

        if (card.isFavorite) {
            if (!isAlreadyFavorite) {
                // Ajouter la carte aux favoris
                Log.d("insert fav", "click $card")
                decksRepository.addToFavorites(userId ?: 0, card.cardId, card)

            } else {
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            Log.d("delete fav", "click $card")
            // Supprimer la carte des favoris
//            val deckCard = decksRepository.getDecksCardById(card.cardId)
//            deckCard?.let { decksRepository.deleteDeckCard(it) }
        }
    }



}