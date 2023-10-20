package com.example.myapplication.model.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.HearthstoneRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InfoViewModel(private val repository: HearthstoneRepository) : ViewModel() {
    private val _cardsLiveData = MutableLiveData<List<String>>()
    val cardsLiveData: MutableLiveData<List<String>> = _cardsLiveData

   // fun fetchCards() {
    //    repository.getCards { cards, error ->
      //      if (cards != null) {
      //          _cardsLiveData.postValue(cards)
      //      } else {
                // Gérer les erreurs ici
       //     }
      //  }
   // }

    fun fetchCards() {
        viewModelScope.launch {
            repository.getCards2().collect {
                _cardsLiveData.postValue(it.classes)
            }
        }
    }


}
