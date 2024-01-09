package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User
import dataBase.AppDatabase
import kotlinx.coroutines.launch

class ProfilViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _navigateToUserFragment = MutableLiveData<Boolean>()
    val navigateToUserFragment: LiveData<Boolean> get() = _navigateToUserFragment

    @Suppress("unused")
    constructor() : this(UserRepository(AppDatabase.getDatabase().userDao())) {
        // Initialisations supplémentaires si nécessaire
    }
    fun deleteAccount(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            Log.d("ProfilViewModel", "Compte supprimé")
            _navigateToUserFragment.value = true

        }
    }

    // Méthode pour signaler que la navigation a été effectuée
    fun onUserFragmentNavigated() {
        Log.d("ProfilViewModel", "UserFragment navigation completed")
        _navigateToUserFragment.value = false
    }
}
