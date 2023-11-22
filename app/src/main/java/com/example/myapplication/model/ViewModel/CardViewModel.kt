package com.example.myapplication.model.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.HearthstoneRepository
import com.example.myapplication.model.Card
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardsRepository) : ViewModel()  {

    private val _cardsLiveData = MutableLiveData<String>()
    private val _cardsList = MutableLiveData<List<Card>>()
    val cardsLiveData: MutableLiveData<String> = _cardsLiveData
    val cardsList: MutableLiveData<List<Card>> = _cardsList


    fun fetchCards2() {
        viewModelScope.launch {
            repository.getCards().collect {

               // _cardsLiveData.postValue(it.Basic.first().cardId)
//                _cardsLiveData.postValue(it.Basic)
                cardsList.postValue(it.Basic)

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