package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.dataSource.APIDataSource.retrofit
import com.example.myapplication.domain.ApiService
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.model.ViewModel.InfoViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.HearthstoneRepository
import com.example.myapplication.factory.AllCardViewModel
import com.example.myapplication.factory.CardViewModelFactory


class MainActivity : AppCompatActivity() {



    private lateinit var infoViewModel: InfoViewModel
    private lateinit var infoViewModelCard: CardViewModel
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        // val cardList: MutableList<Card> = ArrayList()
       // val cardAdapter = CardAdaptater(cardList)
        val button : Button = findViewById(R.id.openCardListButton)
        val button2 : Button = findViewById(R.id.buttonTest)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //recyclerView.adapter = cardAdapter

        val adapter = CardAdaptater(emptyList())
        recyclerView.adapter = adapter

        val hearthstoneApiService = retrofit.create(ApiService::class.java)
        val repository = HearthstoneRepository(hearthstoneApiService)

        infoViewModel = ViewModelProvider(this, CardViewModelFactory(repository)).get(InfoViewModel::class.java)

        infoViewModel.fetchCards()

        button.setOnClickListener{
            val intent = Intent(this, CardEnum::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener{
            val CardApiService = retrofit.create(CardDataService::class.java)
            val Cardrepository = CardsRepository(CardApiService)
            infoViewModelCard = ViewModelProvider(this, AllCardViewModel(Cardrepository)).get(
                CardViewModel::class.java)
            infoViewModelCard.fetchCards2()

        }
    }
}




