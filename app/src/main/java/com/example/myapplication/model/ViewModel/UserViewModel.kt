package com.example.myapplication.model.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.CardsRepository
import com.example.myapplication.domain.repository.DaoUser
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User
import dataBase.AppDatabase
import kotlinx.coroutines.launch

class UserViewModel (private val userRepository: UserRepository) : ViewModel()  {

    private val _userLoggedIn = MutableLiveData<Boolean>()
    val userLoggedIn: LiveData<Boolean> get() = _userLoggedIn
    fun login(username: String, password: String, onLoginResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByUsername(username)
            val isLoginSuccessful = user?.password == password
            onLoginResult(isLoginSuccessful)


        }
    }

}