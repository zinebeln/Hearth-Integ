package com.example.myapplication

import android.app.Application
import timber.log.Timber


class HearthStone : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}