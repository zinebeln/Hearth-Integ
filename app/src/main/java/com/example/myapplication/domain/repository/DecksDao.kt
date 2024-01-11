package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}