package com.example.myapplication.model.ViewModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    // ... d'autres méthodes

    @Suppress("unused")
    constructor() : this(UserRepository()) {
    }
    suspend fun signUp(username: String, password: String): Long {
        val newUser = User(username = username, password = password)
        return userRepository.insertUser(newUser)
    }
}
