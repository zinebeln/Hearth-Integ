package com.example.myapplication.model.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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

    private val _img = MutableLiveData<String?>()
    val img: MutableLiveData<String?> get() = _img

    private val _imageUri = MutableLiveData<String>()
    val imageUri: LiveData<String>
        get() = _imageUri

    fun setImageUri(uri: String) {
        _imageUri.value = uri
    }

    fun setImage(uri: String) {
        _img.value = uri
    }

    fun saveProfileImage(username: String, imagePath: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByUsername(username)
            user?.let {
                it.profileImagePath = imagePath
                //userRepository.insertOrUpdateUser(it)
            }
        }
    }

    @Suppress("unused")
//    constructor() : this(UserRepository(AppDatabase.getDatabase().userDao())) {
        constructor() : this(UserRepository()) {
        // Initialisations supplémentaires si nécessaire
    }
    fun deleteAccount(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            Log.d("ProfilViewModel", "Compte supprimé")
            _navigateToUserFragment.value = true

        }
    }
   fun getProfileImagePath(username : String): String? {
        // Supposons que vous ayez une fonction dans votre repository pour récupérer le chemin de l'image de profil

        viewModelScope.launch {
         val path = userRepository.getProfileImagePath(username)
          _img.value = path
        }
        return _img.value
    }



    // Méthode pour signaler que la navigation a été effectuée
    fun onUserFragmentNavigated() {
        Log.d("ProfilViewModel", "UserFragment navigation completed")
        _navigateToUserFragment.value = false
    }
}
