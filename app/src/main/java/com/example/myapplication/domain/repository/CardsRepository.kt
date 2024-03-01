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

    val cardDao = AppDatabase.getDatabase().cardDao()

    @SuppressLint("SuspiciousIndentation")
    suspend fun getCards(): Flow<Cards> = flow {
        try {
            val response = CardSource.cardSource.getCardsData2()
            if (response.isSuccessful) {
                val cards = response.body() ?: Cards()
                cardDao.insertCards(cards.Basic)
                emit(cards)
            } else {
                emit(Cards())
            }
            Log.d("API_INFO", "All cards in fetchCards card repository: $response")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun searchCards(query: String): List<Card> {
        return cardDao.searchCards(query)
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

