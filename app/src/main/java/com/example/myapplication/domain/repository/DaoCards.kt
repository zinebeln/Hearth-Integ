package com.example.myapplication.domain.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards

@Dao
interface DaoCards {

    @Query("SELECT * FROM Card LIMIT 10000")
    fun getCardsData2():List<Card>

    @Insert
    fun insertCards(cards : List<Card>)


}