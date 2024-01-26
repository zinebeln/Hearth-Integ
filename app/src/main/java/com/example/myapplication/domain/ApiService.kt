package com.example.myapplication.domain

import com.example.myapplication.model.Infos
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.*

interface ApiService {
    @GET("info/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
    suspend fun getCards2(): Infos

}
