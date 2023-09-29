package com.example.myapplication

import com.example.myapplication.model.InfosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("info/?rapidapi-key=83de557c75mshf10e086e2ef22f9p1eb351jsnebdd8be1665d")
    fun getCards(): Call<InfosResponse>


}
