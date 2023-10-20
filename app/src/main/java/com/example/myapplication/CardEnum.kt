package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.model.CardViewModel
import com.example.myapplication.repository.HearthstoneRepository


class CardEnum : AppCompatActivity() {

    private lateinit var cardViewModel: CardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_item_layout)


        val hearthstoneApiService = APIDataSource.retrofit.create(ApiService::class.java)
        val repository = HearthstoneRepository(hearthstoneApiService)

        val cardList: MutableList<Card> = ArrayList()

        val viewModelFactory = CardViewModelFactory(repository)
        cardViewModel = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)

        // Appelez la méthode fetchCards pour récupérer les données
        cardViewModel.fetchCards()

        // Configurez votre interface utilisateur pour afficher les données dans le LiveData de cardViewModel.
        cardViewModel.cardsLiveData.observe(this, Observer { cards ->
            // Mettez à jour l'interface utilisateur avec les données
            if (cards != null) {
                // Par exemple, utilisez une RecyclerView pour afficher les cartes.
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = CardAdaptater(cards)
                Log.d("API_SUCCESS", "Appel boutton list: ${cardList}")
                Log.d("API_SUCCESS", "Appel boutton : ${cards}")

            }
        })
    }
}
