package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.model.DecksCard
import dataBase.AppDatabase

class DecksCardRepository() {

//    private val decksCardDao: DecksDao;


    val decksCardDao = AppDatabase.getDatabase().decksDao();

    suspend fun insertDecksCard(decksCard: DecksCard) {
        decksCardDao.insertDeckCard(decksCard)
    }
    suspend fun deleteDeckCard(decksCard: DecksCard) {
        decksCardDao.deleteDecksCard(decksCard)
    }

    suspend fun getIdDeckCard(cardId: String){
        decksCardDao.getDecksCardById(cardId)
    }

    fun getAllDecksCards(): LiveData<List<DecksCard>> {
        return decksCardDao.getAllDeckCards()
    }
}