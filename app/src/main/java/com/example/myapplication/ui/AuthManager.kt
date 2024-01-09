package com.example.myapplication.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.User

class AuthManager(context: Context) {
    private val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val isLoggedInKey = "isLoggedIn"
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    fun setLoggedIn(isLoggedIn: Boolean) {
        preferences.edit().putBoolean(isLoggedInKey, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(isLoggedInKey, false)
    }

    fun setUser(user: User) {
        Log.d("AuthManager", "User set: $user")
        _user.value = user
    }
}
