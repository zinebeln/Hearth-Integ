package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.ApiService
import com.example.myapplication.Card
import retrofit2.Call
import retrofit2.Response

class HearthstoneRepository(private val hearthstoneApiService: ApiService) {
    // Méthode pour récupérer les cartes depuis l'API
    fun getCards(callback: (List<Card>?, Throwable?) -> Unit) {
        APIDataSource.hearthstoneApiService.getCards().enqueue { response, throwable ->
            if (response != null && response.isSuccessful) {
                // Traitez la réponse ici
                val cards = response.body()
                Log.d("API_INFO", "Version: ${response.body()?.patch}")
                if (cards != null) {
                    // Faites quelque chose avec les cartes
                    Log.d("API_INFO", "Patch: ${response}")
                }
                cards?.classes?.forEach { card ->
                    Log.d("API_SUCCESS", "Appel API réussi. Nombre de cartes récupérées : ${card}")

                    // Vous pouvez ajouter d'autres propriétés de la carte ici
                }
            } else {
                // Gérez les erreurs ici en cas de réponse non réussie ou d'échec de la requête
                Log.e("API_ERROR", "Erreur lors de l'appel API. Code d'erreur : ${response}")

                if (throwable != null) {
                    // Gérez les erreurs de réseau ici (throwable contient l'exception)
                    Log.e("NETWORK_ERROR", "Erreur réseau : ${throwable.message}")
                } else {
                    // Gérez les erreurs de réponse ici
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

