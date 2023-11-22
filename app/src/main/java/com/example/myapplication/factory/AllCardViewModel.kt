package com.example.myapplication.factory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.model.Card


class AllCardViewModel(private val repository: CardsRepository) : ViewModelProvider.Factory  {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
                return CardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }



}