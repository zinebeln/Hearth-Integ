package com.example.myapplication.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Card
import com.example.myapplication.repository.HearthstoneRepository

class CardViewModel(private val repository: HearthstoneRepository) : ViewModel() {
    private val _cardsLiveData = MutableLiveData<List<String>>()
    val cardsLiveData: MutableLiveData<List<String>> = _cardsLiveData

    fun fetchCards() {
        repository.getCards { cards, error ->
            if (cards != null) {
                _cardsLiveData.postValue(cards)
            } else {
                // Gérer les erreurs ici
            }
        }
    }


}
