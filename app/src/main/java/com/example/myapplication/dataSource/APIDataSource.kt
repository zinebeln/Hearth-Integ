package com.example.myapplication.dataSource

import com.example.myapplication.domain.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIDataSource {
    val intercepter = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().apply {
        this.addInterceptor(intercepter)
    }.build()

    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://omgvamp-hearthstone-v1.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val hearthstoneApiService = retrofit.create(ApiService::class.java)
}