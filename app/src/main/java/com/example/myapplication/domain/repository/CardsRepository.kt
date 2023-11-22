package com.example.myapplication.domain.repository

import android.util.Log
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.dataSource.CardSource
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

class CardsRepository(private val CardApiService: CardDataService) {

    suspend fun getCards(): Flow<Cards> = flow {
        val toto = CardSource.cardSource.getCardsData2()

        if (toto.isSuccessful) {
            val test = toto.body()?.Basic
            emit(toto.body() ?: Cards())
        }


        Log.d("API_INFO", "All cards in getCards cardrepository: ${toto}")
        //Log.d("API_INFO", "test: ${test}")
    }


        fun getAllCards(callback: (String?, Throwable?) -> Unit) {
                CardSource.cardSource.getCardsData().enqueue { response, throwable ->
                if (response != null && response.isSuccessful) {
                    val cards = response.body()
                    callback(cards?.cardId, null)
                    Log.d("API_INFO", "Types: ${response.body()?.cardId}")
                    if (cards != null) {
                        Log.d("API_INFO", "All cards: ${response}")
                    }
                    //cards?.classes?.forEach { card ->
                   //     Log.d("API_SUCCESS", "Appel API réussi. Nombre de cartes récupérées : ${card}")
                 //   }
                } else {
                    Log.e("API_ERROR", "Erreur lors de l'appel API. Code d'erreur : ${response}")
                    if (throwable != null) {
                        Log.e("NETWORK_ERROR", "Erreur réseau : ${throwable.message}")
                    } else {
                        Log.e("NETWORK_ERROR", "Erreur réseau : ")
                    }
                }
            }
        }
        fun <T> Call<T>.enqueue(callback: (Response<T>?, Throwable?) -> Unit) {
            enqueue(object : retrofit2.Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    callback(response, null)
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    callback(null, t)
                }
            })
        }
    }

