package com.example.myapplication.domain.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.dataSource.CardSource
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards
import dataBase.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

class CardsRepository() {

    private val cardDao = AppDatabase.getDatabase().cardDao()
    suspend fun searchCards(query: String): List<Card> {
        return cardDao.searchCards(query)
    }

    suspend fun insertCards(cards : List<Card>) {
        return cardDao.insertCards(cards)
    }

    suspend fun getAllcard() : List<Card> {
        return cardDao.getCardsData()
    }

    suspend fun getCardsFromApi(): List<Card> {
        val response = CardSource.cardSource.getCardsData2()
        val cards = response.body() ?: Cards()
        return cards.Basic
    }

    suspend fun getCardsSortedByType(): List<Card> {
        return cardDao.getCardsSortedByType()
    }

    suspend fun getCardsSortedByAttack(): List<Card> {
        return cardDao.getCardsSortedByAttack()
    }

    suspend fun getCardsSortedByName(): List<Card> {
        return cardDao.getCardsSortedByName()
    }


}

