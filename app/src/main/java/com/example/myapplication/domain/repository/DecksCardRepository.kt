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

    suspend fun insertDecksCard2(decksCard: DecksCard) {
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

    fun getAllDecksCards2(userId : Long): LiveData<List<DecksCard>> {
        return decksCardDao.getDeckCardsForUser(userId)
    }
    //changement ici
//    suspend fun getDeckCardsForUser(userId: String): List<DecksCard> {
//        return decksCardDao.getDeckCardsForUser(userId)
//    }
}