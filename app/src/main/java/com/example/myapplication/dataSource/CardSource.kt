package com.example.myapplication.dataSource

import com.example.myapplication.domain.CardDataService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CardSource {

        private val intercepter = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        private val client = OkHttpClient.Builder().apply {
            this.addInterceptor(intercepter)
            this.connectTimeout(2, TimeUnit.MINUTES)
            this.readTimeout(2, TimeUnit.MINUTES)
            this.writeTimeout(2, TimeUnit.MINUTES)
        }.build()

        private val gson = GsonBuilder().setLenient().create()
        private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://omgvamp-hearthstone-v1.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))

            .build()
        val cardSource = retrofit.create(CardDataService::class.java)
    }
