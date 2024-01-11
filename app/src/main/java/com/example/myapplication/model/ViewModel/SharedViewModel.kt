package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.DecksCardRepository
import com.example.myapplication.model.Card

class SharedViewModel : ViewModel() {
    private val _selectedCard = MutableLiveData<Card>()


    val selectedCard: LiveData<Card> get() = _selectedCard
    fun selectCard(card: Card) {
        Log.d("Sharedviewmodel", "select card $card")
        _selectedCard?.value = card
        _selectedCard.postValue(card)
    }



}
