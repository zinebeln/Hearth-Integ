package com.example.myapplication.model.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.HearthstoneRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardsRepository) : ViewModel()  {

    private val _cardsLiveData = MutableLiveData<String>()
    val cardsLiveData: MutableLiveData<String> = _cardsLiveData


    fun fetchCards2() {
        viewModelScope.launch {
            repository.getCards().collect {
                _cardsLiveData.postValue(it.classes)
            }
        }
    }
    fun fetchCards() {
        repository.getAllCards() { cards, error ->
            if (cards != null) {
                _cardsLiveData.postValue(cards)
            } else {
                // Gérer les erreurs ici
            }
        }
    }

}