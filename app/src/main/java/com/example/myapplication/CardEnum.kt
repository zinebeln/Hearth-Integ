package com.example.myapplication

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.factory.AllCardViewModel



class CardEnum : AppCompatActivity() {


    private lateinit var infoViewModel: CardViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_item_layout)

    }
}
