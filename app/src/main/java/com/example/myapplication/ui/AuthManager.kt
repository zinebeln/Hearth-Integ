package com.example.myapplication.ui

import android.content.Context

class AuthManager(context: Context) {
    private val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val isLoggedInKey = "isLoggedIn"

    fun setLoggedIn(isLoggedIn: Boolean) {
        preferences.edit().putBoolean(isLoggedInKey, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(isLoggedInKey, false)
    }
}
