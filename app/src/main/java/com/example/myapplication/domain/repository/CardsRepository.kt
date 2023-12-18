package com.example.myapplication.domain.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.dataSource.CardSource
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

class CardsRepository() {


    @SuppressLint("SuspiciousIndentation")
    suspend fun getCards(): Flow<Cards> = flow {

      val toto = CardSource.cardSource.getCardsData2()
        //val toto = CardApiService.getCardsData2()
        if (toto.isSuccessful) {
            val cards = toto.body() ?: Cards()
            emit(cards)
        } else {
            emit(Cards()) // Ou vous pouvez émettre un état d'erreur si nécessaire
        }
        Log.d("API_INFO", "All cards in getCards cardrepository: ${toto}")
    }

    }

