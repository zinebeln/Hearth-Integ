package com.example.myapplication.domain.repository

import android.util.Log
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.domain.ApiService
import com.example.myapplication.model.Infos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

class HearthstoneRepository(private val hearthstoneApiService: ApiService) {

    suspend fun getCards2(): Flow<Infos> = flow {
        val toto = APIDataSource.hearthstoneApiService.getCards2()
        emit(toto)
        Log.d("API_INFO", "All info: ${toto}")

    }
    fun getCards(callback: (List<String>?, Throwable?) -> Unit) {
        APIDataSource.hearthstoneApiService.getCards().enqueue { response, throwable ->
            if (response != null && response.isSuccessful) {
                val cards = response.body()
                callback(cards?.classes, null)
                Log.d("API_INFO", "Version: ${response.body()?.patch}")
                if (cards != null) {
                    Log.d("API_INFO", "Patch: ${response}")
                }
                cards?.classes?.forEach { card ->
                    Log.d("API_SUCCESS", "Appel API réussi. Nombre de cartes récupérées : ${card}")
                }
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

