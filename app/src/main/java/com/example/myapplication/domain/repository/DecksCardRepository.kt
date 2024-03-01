package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard
import dataBase.AppDatabase

class DecksCardRepository() {

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
   suspend fun decksCardFav(userId: Long) : List<DecksCard> {
        return decksCardDao.getDecksCardsForUser(userId)
    }
    suspend fun getFavoriteCards(): List<DecksCard> {
        return decksCardDao.getFavoriteCards()
    }

    suspend fun addToFavorites(userId: Long, cardsId: String, card: Card) {
        // Vérifiez si la carte n'est pas déjà dans les favoris de l'utilisateur
        if (decksCardDao.getDecksCardById(cardsId) == null) {
            // Créez un nouveau DecksCard avec l'ID de l'utilisateur et l'ID de la carte
            val decksCard = DecksCard(userId = userId, cardsId = cardsId, card = card)
            // Insérez la nouvelle carte dans la base de données
            insertDecksCard(decksCard)
        }
    }



}