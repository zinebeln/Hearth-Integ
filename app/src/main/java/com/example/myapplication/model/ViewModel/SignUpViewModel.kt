package com.example.myapplication.model.ViewModel

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    // ... d'autres méthodes

    @Suppress("unused")
    constructor() : this(UserRepository()) {
    }
    suspend fun isUsernameAvailable(username: String): Boolean {
        return userRepository.isUsernameAvailable(username)
    }
    suspend fun registerUser(username: String, password: String): Boolean {
        // Vérifiez d'abord si le nom d'utilisateur est disponible
        if (!isUsernameAvailable(username)) {
            // Le nom d'utilisateur est déjà pris, affichez un message d'erreur
            return false
        }
        // Le nom d'utilisateur est disponible, insérez le nouvel utilisateur dans la base de données
        userRepository.insertUser(User(username = username, password = password, profileImagePath = ""))
        return true
    }
    suspend fun signUp(username: String, password: String): Long {
        val newUser = User(username = username, password = password, profileImagePath = "")
        return userRepository.insertUser(newUser)
    }
}
