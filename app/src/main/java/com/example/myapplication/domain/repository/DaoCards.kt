package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards

@Dao
interface DaoCards {

    @Query("SELECT * FROM Card LIMIT 10000")
    fun getCardsData2():List<Card>
    @Query("SELECT * FROM Card LIMIT 10000")
    suspend fun getCardsData():List<Card>
    @Query("SELECT * FROM Card WHERE Card.name LIKE '%' || :query || '%'")
    suspend fun searchCards(query: String): List<Card>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards : List<Card>)
    @Query("SELECT * FROM Card WHERE (:type IS NULL OR type = :type) AND (:attack IS NULL OR attack = :attack)")
    suspend fun getFilteredCards(type: String?, attack: Int?): List<Card>
    @Query("SELECT * FROM Card ORDER BY type ASC")
    suspend fun getCardsSortedByType(): List<Card>
    @Query("SELECT * FROM Card ORDER BY attack ASC")
    suspend fun getCardsSortedByAttack(): List<Card>
    @Query("SELECT * FROM Card ORDER BY name ASC")
    suspend fun getCardsSortedByName(): List<Card>

}