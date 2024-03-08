package com.example.myapplication.model.ViewModel

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    @Suppress("unused")
    constructor() : this(UserRepository()) { }
    suspend fun isUsernameAvailable(username: String): Boolean {
        return userRepository.isUsernameAvailable(username)
    }
    suspend fun signUp(username: String, password: String): Long {
        val newUser = User(username = username, password = password, profileImagePath = "")
        return userRepository.insertUser(newUser)
    }
}
