package com.example.myapplication

import android.app.Application
import android.content.Context
import dataBase.AppDatabase

class HearthStone : Application() {
    override fun onCreate() {
        super.onCreate()
        //Timber.plant(Timber.DebugTree())
        AppDatabase.initDatabase(applicationContext)

    }
}