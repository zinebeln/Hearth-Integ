package com.example.myapplication.model.ViewModel

import android.util.Log
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

//    class DecksViewModel (private val decksRepository: DecksCardRepository, private val userRepository: UserRepository,  private val authManager: AuthManager) : ViewModel() {
//

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

    private lateinit var auth: AuthManager

    private val userIdObserver = Observer<Long> { userId ->
      
    }


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

    fun refreshDeckCards2() {
        viewModelScope.launch {
            // Récupérez la nouvelle liste des cartes depuis le référentiel
            val nullableLong: Long? = auth.user.value?.userId
            val nonNullableLong: Long = nullableLong ?: 0L // Utilisez une valeur par défaut, par exemple 0L

            val updatedDeckCards = decksRepository.getAllDecksCards2(nonNullableLong)

            _d.postValue(updatedDeckCards.value)
        }
    }
    suspend fun toggleFavoriteStatus(card: Card) {
        //val userId = userRepository.getLoggedInUserId() ?: return
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
//                decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, userId = userId, card = card))
               // decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, card = card))
            }else{
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            // Supprimer la carte des favoris
//            decksRepository.deleteDeckCard(DecksCard(cardId = card.cardId, card = card))
            Log.d("dekete fav", "click $card")
         //  decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, card = card))
           //decksRepository.deleteDeckCard(DecksCard(cardId = card.cardId, card = card))

        }
    }


    suspend fun toggleFavoriteStatus2(card: Card, userId : Long?) {
        //val userId = userRepository.getLoggedInUserId() ?: return
        Log.d("togglefavorite", "click $card")
        // Vérifiez si la carte est déjà en favori


        val isAlreadyFavorite = decksRepository.getIdDeckCard(card.cardId) != null
        Log.d("isAlreadyFavorite", "click $isAlreadyFavorite")
        card.isFavorite = !card.isFavorite

        // Mettez à jour la LiveData indiquant si la carte est en favori ou non
        _isCardFavorite.value = card.isFavorite

       // userRepository.getUserId()


//        val nullableLong: Long? = auth.user.value?.userId
//        val nonNullableLong: Long = nullableLong ?: 0L // Utilisez une valeur par défaut, par exemple 0L
        if (card.isFavorite) {
            if(!isAlreadyFavorite) {
                // Ajouter la carte aux favoris
                Log.d("insert fav", "click $card")
//                decksRepository.insertDecksCard(DecksCard(cardId = card.cardId, userId = userId, card = card))
                decksRepository.insertDecksCard2(DecksCard(cardsId = card.cardId, card = card, userId = userId ))
//                decksRepository.insertDecksCard2(DecksCard(cardsId = card.cardId, card = card, userId = nonNullableLong ))
            }else{
                _errorMessage.value = R.string.card_already_in_favorites.toString()
            }
        } else {
            // Supprimer la carte des favoris
//            decksRepository.deleteDeckCard(DecksCard(cardId = card.cardId, card = card))
            Log.d("dekete fav", "click $card")
//            decksRepository.insertDecksCard2(DecksCard(cardsId = card.cardId, card = card, userId = nonNullableLong))
            //decksRepository.deleteDeckCard(DecksCard(cardId = card.cardId, card = card))
            Log.d("user id $userId ", "test");
            decksRepository.insertDecksCard2(DecksCard(cardsId = card.cardId, card = card, userId = userId ))

        }
    }




//    suspend fun addDeckCardToUser(deckId: Long, cardId: String, card: Card) {
//        val userId = authManager.getLoggedInUserId() ?: return
//        val deckCard = DecksCard(deckId, cardId, userId, card)
//        decksRepository.insertDecksCard(deckCard)
//        refreshDeckCardsForLoggedInUser()
//    }

//    suspend fun getDeckCardsForLoggedInUser(): List<DecksCard> {
//        val userId = authManager.getLoggedInUserId() ?: return emptyList()
//        return decksRepository.getDeckCardsForUser(userId)
//    }
//    suspend fun refreshDeckCardsForLoggedInUser() {
//        val userDecksCards = getDeckCardsForLoggedInUser()
//        _d.postValue(userDecksCards)
//    }

}