package com.example.myapplication.model.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.model.Card
import kotlinx.coroutines.launch


class CardViewModel(private val repository: CardsRepository) : ViewModel()  {

    private val _searchResults = MutableLiveData<List<Card>>()
    val searchResults: LiveData<List<Card>> = _searchResults

    private val _filteredType= MutableLiveData<List<Card>>()
    val filteredType: LiveData<List<Card>> = _filteredType
    private val _filteredCost = MutableLiveData<List<Card>>()
    val filteredCost: LiveData<List<Card>> = _filteredCost
    private val _filteredAtt= MutableLiveData<List<Card>>()
    val filteredAtt: LiveData<List<Card>> = _filteredAtt

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _cardsList = MutableLiveData<List<Card>>()
    val cardsList: LiveData<List<Card>> = _cardsList

    init {
        _isLoading.value = false
    }
    @Suppress("unused")
    constructor() : this(CardsRepository()) {
    }
    fun searchCards(query: String) {
        viewModelScope.launch {
            val results = repository.searchCards(query)
            _searchResults.postValue(results)
        }
    }

    fun fetchCards() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cards = repository.getCardsFromApi()
                //cardDao.insertCards(cards)
                repository.insertCards(cards)
                _cardsList.postValue(cards)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCardsFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
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

}



