package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback



import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.dataSource.APIDataSource.retrofit
import com.example.myapplication.model.CardViewModel
import com.example.myapplication.repository.HearthstoneRepository


class MainActivity : AppCompatActivity() {

    //val retrofit = Retrofit.Builder()
      //  .baseUrl("https://omgvamp-hearthstone-v1.p.rapidapi.com/info/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
      //    .addConverterFactory(GsonConverterFactory.create())
      //  .build()

    // Initialisez Retrofit avec le convertisseur Gson
    private lateinit var cardViewModel: CardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val cardList: MutableList<Card> = ArrayList()
        val cardAdapter = CardAdaptater(cardList)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = cardAdapter

        val adapter = CardAdaptater(emptyList())
        recyclerView.adapter = adapter

        val hearthstoneApiService = retrofit.create(ApiService::class.java)
        val repository = HearthstoneRepository(hearthstoneApiService)

        cardViewModel = ViewModelProvider(this, CardViewModelFactory(repository)).get(CardViewModel::class.java)

        cardViewModel.cardsLiveData.observe(this, Observer { cards ->
            // Mettez à jour votre adaptateur avec les données
            //adapter.updateData(cards)

            Toast.makeText(this, cards.toString(), Toast.LENGTH_SHORT).show()
        })

        cardViewModel.fetchCards()

        /*
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
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback(response, null)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback(null, t)
            }
        })

         */
    }






}




