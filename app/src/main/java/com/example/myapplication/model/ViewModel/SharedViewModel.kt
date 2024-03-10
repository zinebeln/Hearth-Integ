package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.DecksCardRepository
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard

class SharedViewModel : ViewModel() {
    private val _selectedCard = MutableLiveData<Card>()
    val selectedCard: LiveData<Card> get() = _selectedCard

    private val _selectedCardDeck = MutableLiveData<DecksCard>()
    val selectedCardDeck: LiveData<DecksCard> get() = _selectedCardDeck
    fun selectCard(card: Card) {
        Log.d("Sharedviewmodel", "select card $card")
        _selectedCard?.value = card
        _selectedCard.postValue(card)
    }

    fun selectCardDeck(card: DecksCard) {
        Log.d("Sharedviewmodel", "select card $card")
        _selectedCardDeck?.value = card
        _selectedCardDeck.postValue(card)
    }



}
