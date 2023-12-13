package com.example.myapplication.domain

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.model.Card
import com.example.myapplication.model.Cards
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

@Dao
interface CardDataService {

    @Query("SELECT * FROM Card")
    @GET("cards/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
    fun getCardsData(): Call<Card>


    @GET("cards/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
    suspend fun getCardsData2(): Response<Cards>

}