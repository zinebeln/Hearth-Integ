package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard


@Dao
interface DecksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeckCard(decksCard: DecksCard)
    @Delete
    suspend fun deleteDecksCard(decksCard: DecksCard)

    @Query("SELECT * FROM decks_cards")
    fun getAllDeckCards(): LiveData<List<DecksCard>>

    @Query("SELECT * FROM decks_cards WHERE cardId = :cardId")
    suspend fun getDecksCardById(cardId: String): DecksCard?

    @Query("SELECT * FROM decks_cards WHERE userId = :userId")
    fun getDeckCardsForUser(userId: Long): LiveData<List<DecksCard>>

    @Query("SELECT * FROM decks_cards WHERE userId = :userId")
    suspend fun getDecksCardsForUser(userId: Long): List<DecksCard>

    @Query("SELECT * FROM decks_cards WHERE isFavorite = 1")
    suspend fun getFavoriteCards(): List<DecksCard>

    @Query("SELECT id FROM decks_cards WHERE isFavorite = 1")
    suspend fun getFavoriteCardsID(): List<DecksCard>

    @Query("DELETE FROM decks_cards WHERE userId = :userId AND cardId = :cardId")
    suspend fun deleteUserCardCrossRef(userId: Long, cardId: String)


}