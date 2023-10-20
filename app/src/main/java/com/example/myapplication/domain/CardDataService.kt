package com.example.myapplication.domain

import com.example.myapplication.model.Card
import retrofit2.Call
import retrofit2.http.GET

interface CardDataService {

    @GET("cards/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
    fun getCardsData(): Call<Card>

    @GET("cards/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
    suspend fun getCardsData2(): Card

}