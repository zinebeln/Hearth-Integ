package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.HearthstoneRepository
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards
import dataBase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.filter



class CardViewModel(private val repository: CardsRepository) : ViewModel()  {

    @Suppress("unused")
    constructor() : this(CardsRepository()) {
    }

    private val _cardsList = MutableLiveData<List<Card>>()
    val cardsList: LiveData<List<Card>> = _cardsList
    private var cardDao = AppDatabase.getDatabase().cardDao()

    suspend fun fetchCards2() {
        viewModelScope.launch {
            try {
                repository.getCards().collect { cards ->
                    Log.d("CardViewModel", "Received cards from repository: $cards")
                    val cardsWithImages = withContext(Dispatchers.IO) {
                        // Utiliser la fonction du DAO ici
//                        cardDao.getCardsData2()
                        val localData = cardDao.getCardsData2()
                        Log.d("CardViewModel", "Local data from database: $localData")
                        localData
                    }
                    _cardsList.postValue(cardsWithImages.filter { it.img != null })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}



