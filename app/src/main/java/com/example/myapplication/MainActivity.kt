package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup


import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.dataSource.APIDataSource
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.model.ViewModel.CardViewModel
import com.example.myapplication.model.ViewModel.InfoViewModel
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.factory.AllCardViewModel
import com.example.myapplication.ui.CardFragment
import dataBase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        val buttonOpenFragment = findViewById<Button>(R.id.buttonOpenFragment)

            val car = APIDataSource.retrofit.create(CardDataService::class.java)
            val repository = CardsRepository(car)
            val viewModelFactory = AllCardViewModel(repository)
            infoViewModelCard = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)
            infoViewModelCard.fetchCards2()


        // Observer les changements de la liste de cartes ici
        infoViewModelCard.cardsList.observe(this, Observer { cards ->
            if (cards != null) {
                buttonOpenFragment.setOnClickListener {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    lifecycleScope.launch(Dispatchers.IO) {
                        val data = AppDatabase.getDatabase().cardDao().getCardsData2()
                        if (data.isEmpty()) {
                            AppDatabase.getDatabase().cardDao().insertCards(cards)
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            val fragment = CardFragment.newInstance(data.firstOrNull())
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }

                    }
                }
            }
        })


    }
}




