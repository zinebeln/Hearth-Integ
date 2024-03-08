package com.example.myapplication.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User
import kotlinx.coroutines.launch

class AuthManager(context: Context) {

    private val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val isLoggedInKey = "isLoggedIn"
    private val userIdKey = "userId"

    private val _userLoggedIn = MutableLiveData<Boolean>()
    val userLoggedIn: LiveData<Boolean> = _userLoggedIn

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    init {
        _userLoggedIn.value = isLoggedIn()
        val userId = preferences.getLong(userIdKey, 0)
        if (userId != 0L) {
            // Charge l'utilisateur depuis la source de données appropriée (par exemple, votre base de données) en utilisant l'ID stocké.
            // user.postValue(loadUserById(userId))
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean, userId: Long? = null) {
        _userLoggedIn.value = isLoggedIn
        if (isLoggedIn) {
            userId?.let {
                preferences.edit().putLong(userIdKey, it).apply()
                setUserId(it)
            }
        } else {
            preferences.edit().remove(userIdKey).apply()
            // Supprime l'utilisateur de votre source de données.
            // deleteUser()
        }
    }

    fun isLoggedIn(): Boolean {
       return preferences.getBoolean(isLoggedInKey, false)
    }

    fun setUser(user: User) {
        Log.d("AuthManager", "User set: $user")
        _user.value = user
    }
    fun setUserId(userId: Long) {
        _userId.value = userId
    }

}
