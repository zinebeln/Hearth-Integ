package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard
import dataBase.AppDatabase

class DecksCardRepository() {

    private val decksCardDao = AppDatabase.getDatabase().decksDao();

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
   suspend fun decksCardFav(userId: Long) : List<DecksCard> {
        return decksCardDao.getDecksCardsForUser(userId)
    }
    suspend fun getFavoriteCards(): List<DecksCard> {
        return decksCardDao.getFavoriteCards()
    }
    suspend fun addToFavorites(userId: Long, cardsId: String, card: Card) {

        if (decksCardDao.getDecksCardById(cardsId) == null) {
            val decksCard = DecksCard(userId = userId, cardsId = cardsId, card = card)
            insertDecksCard(decksCard)
        }
    }
}