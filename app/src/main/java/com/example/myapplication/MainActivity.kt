package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup


import android.widget.Button
import android.widget.FrameLayout
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

    private lateinit var infoViewModelCard: CardViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)
        val buttonOpenFragment = findViewById<Button>(R.id.buttonOpenFragment)

        buttonOpenFragment.setOnClickListener {
            // Remplacez le contenu du conteneur par votre fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CardFragment())
                .addToBackStack(null)
                .commit()
        }



//            val car = APIDataSource.retrofit.create(CardDataService::class.java)
//            val repository = CardsRepository(car)
//            val viewModelFactory = AllCardViewModel(repository)
//            infoViewModelCard = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)
//            infoViewModelCard.fetchCards2()

//        infoViewModelCard.cardsList.observe(this, Observer { cards ->
//            if (cards != null) {
//                buttonOpenFragment.setOnClickListener {
//                    val fragmentManager = supportFragmentManager
//                    val fragmentTransaction = fragmentManager.beginTransaction()
//
//                    lifecycleScope.launch(Dispatchers.IO) {
//                        val data = AppDatabase.getDatabase().cardDao().getCardsData2()
//                        if (data.isEmpty()) {
//                            Log.d("api main", "test: ${cards}")
//                            AppDatabase.getDatabase().cardDao().insertCards(cards)
//                        }
//                        lifecycleScope.launch(Dispatchers.Main) {
//                            val fragment = CardFragment.newInstance(data.firstOrNull())
//                            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
//                            fragmentTransaction.addToBackStack(null)
//                            fragmentTransaction.commit()
//                        }
//
//                    }
//                }
//            }
//        })


    }
}




