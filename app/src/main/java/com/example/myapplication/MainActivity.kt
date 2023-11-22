package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.dataSource.APIDataSource.retrofit
import com.example.myapplication.domain.ApiService
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.model.ViewModel.InfoViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.HearthstoneRepository
import com.example.myapplication.factory.AllCardViewModel
import com.example.myapplication.factory.CardViewModelFactory
import com.example.myapplication.model.Card
import com.example.myapplication.model.CardsList
import com.example.myapplication.ui.CardFragment
import dataBase.AppDatabase


class MainActivity : AppCompatActivity() {



    private lateinit var infoViewModel: InfoViewModel
    private lateinit var infoViewModelCard: CardViewModel






    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {


      //  super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_main)

      //  val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        // val cardList: MutableList<Card> = ArrayList()
       // val cardAdapter = CardAdaptater(cardList)
        // val button : Button = findViewById(R.id.openCardListButton)
       // val button2 : Button = findViewById(R.id.buttonTest)

      //  val layoutManager = LinearLayoutManager(this)
     //   recyclerView.layoutManager = layoutManager
        //recyclerView.adapter = cardAdapter

      //  val adapter = CardAdaptater(emptyList())
      //  recyclerView.adapter = adapter

       // val hearthstoneApiService = retrofit.create(ApiService::class.java)
      //  val repository = HearthstoneRepository(hearthstoneApiService)

       // infoViewModel = ViewModelProvider(this, CardViewModelFactory(repository)).get(InfoViewModel::class.java)

      //  infoViewModel.fetchCards()

       // button.setOnClickListener{
            //val intent = Intent(this, CardEnum::class.java)
           // startActivity(intent)


       // }

       // button2.setOnClickListener{
        //    val CardApiService = retrofit.create(CardDataService::class.java)
       //    val Cardrepository = CardsRepository(CardApiService)
          //  infoViewModelCard = ViewModelProvider(this, AllCardViewModel(Cardrepository)).get(
         //       CardViewModel::class.java)
         //   infoViewModelCard.fetchCards2()

       // }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            var t : List<Card>;
        val buttonOpenFragment = findViewById<Button>(R.id.buttonOpenFragment)

            var cardsList: CardsList

            var test : List<String>
            val car = APIDataSource.retrofit.create(CardDataService::class.java)
            val repository = CardsRepository(car)
            val viewModelFactory = AllCardViewModel(repository)
            infoViewModelCard = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)
            infoViewModelCard.fetchCards2()
            infoViewModelCard.cardsList.observe(this, Observer { cards ->
                if (cards != null) {
                   // val cardList = CardsList(cards.map { Card(it, "", "", "", "", "", "", "", "", "") })
                    t = cards
                    Log.d("API_SUCCESS", "Appel boutton : ${cards}")
                    Log.d("API_SUCCESS", "Appel boutton : ${cards.first()}")
                }

            })


        buttonOpenFragment.setOnClickListener {

            // Créez un FragmentTransaction
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Instanciez le fragment que vous souhaitez afficher
           // val fragment = CardFragment.newInstance()

            // Ajoutez le fragment à la transaction (utilisez "R.id.fragmentContainer" comme conteneur)
           // fragmentTransaction.replace(R.id.fragmentContainer, fragment)

            fragmentTransaction.addToBackStack(null) // Ajoute le fragment à la pile de retour

            // Effectuez la transaction
            fragmentTransaction.commit()

        }









    }
}




