package com.example.myapplication.model.ViewModel

import android.annotation.SuppressLint
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

    private val _searchResults = MutableLiveData<List<Card>>()
    val searchResults: LiveData<List<Card>> = _searchResults

    private val _filteredType= MutableLiveData<List<Card>>()
    val filteredType: LiveData<List<Card>> = _filteredType
    private val _filteredCost = MutableLiveData<List<Card>>()
    val filteredCost: LiveData<List<Card>> = _filteredCost
    private val _filteredAtt= MutableLiveData<List<Card>>()
    val filteredAtt: LiveData<List<Card>> = _filteredAtt

    fun searchCards(query: String) {
        viewModelScope.launch {
            val results = repository.searchCards(query)
            _searchResults.postValue(results)
        }
    }
    @Suppress("unused")
    constructor() : this(CardsRepository()) {
    }



    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _cardsList = MutableLiveData<List<Card>>()
    val cardsList: LiveData<List<Card>> = _cardsList
    private var cardDao = AppDatabase.getDatabase().cardDao()
    init {
        _isLoading.value = false
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun fetchCards2() {
//        _isLoading.value = true

        if (_cardsList.value.isNullOrEmpty() && _isLoading.value == false)  {
          _isLoading.value = true
                try {
                    repository.getCards().collect { cards ->
                        Log.d("CardViewModel", "Received cards from repository: $cards")
                        val cardsWithImages = withContext(Dispatchers.IO) {
                           cardDao.getCardsData2()
                        }
//                    _cardsList.postValue(cardsWithImages.filter { it.img != null })
                        _cardsList.postValue(cardsWithImages)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }
    }

    suspend fun fetchCards() {
        _isLoading.value = true
        try {
            val cards = repository.getCardsFromApi()
            // Mise à jour de la base de données locale avec les nouvelles données
            cardDao.insertCards(cards)
            _cardsList.postValue(cards)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }

    fun loadCardsFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
//             val cards = cardDao.getCardsData()
               val cards = repository.getAllcard()
                _cardsList.postValue(cards)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getCardsSortedByType() {
        viewModelScope.launch {
            val type = repository.getCardsSortedByType()
            _filteredType.postValue(type)
        }
    }

    fun getCardsSortedByAttack(){
        viewModelScope.launch {
            val type = repository.getCardsSortedByAttack()
            _filteredCost.postValue(type)
        }
    }

    fun getCardsSortedByName(){
        viewModelScope.launch {
            val type = repository.getCardsSortedByName()
            _filteredAtt.postValue(type)
        }
    }



  // }


}



