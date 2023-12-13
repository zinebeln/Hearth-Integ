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

        val car = APIDataSource.retrofit.create(CardDataService::class.java)
        val repository = CardsRepository(car)

        val cardList: MutableList<String> = ArrayList()

//        val viewModelFactory = AllCardViewModel(repository)
//        infoViewModel = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)
//
//        // Appelez la méthode fetchCards pour récupérer les données
//        infoViewModel.fetchCards2()
//
//        // Configurez votre interface utilisateur pour afficher les données dans le LiveData de cardViewModel.
//        infoViewModel.cardsLiveData.observe(this, Observer { cards ->
//            // Mettez à jour l'interface utilisateur avec les données
//            if (cards != null) {
//                // Par exemple, utilisez une RecyclerView pour afficher les cartes.
//                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//                //recyclerView.layoutManager = LinearLayoutManager(this)
//               // recyclerView.adapter = CardAdaptater(cards)
//                Log.d("API_SUCCESS", "Appel boutton list: ${cardList}")
//                Log.d("API_SUCCESS", "Appel boutton : ${cards}")
//
//            }
//        })
    }
}
