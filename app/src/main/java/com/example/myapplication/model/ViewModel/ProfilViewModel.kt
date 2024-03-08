package com.example.myapplication.model.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.ProfilView
import kotlinx.coroutines.launch


class ProfilViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _viewState = MutableLiveData<ProfilView>()
    val viewState: LiveData<ProfilView> = _viewState

    private val _navigateToUserFragment = MutableLiveData<Boolean>()
    val navigateToUserFragment: LiveData<Boolean> get() = _navigateToUserFragment

    private val _img = MutableLiveData<String?>()
    val img: MutableLiveData<String?> get() = _img

    private val _imageUri = MutableLiveData<String>()
    val imageUri: LiveData<String>
        get() = _imageUri
    private val _updateProfileImageStatus = MutableLiveData<Boolean>()
    val updateProfileImageStatus: LiveData<Boolean> = _updateProfileImageStatus

    fun updateProfileImage(userId: Long, imageUri: String?) {
        _updateProfileImageStatus.value = true

        viewModelScope.launch {
            try {
                if (imageUri != null) {
                    userRepository.updateUserProfileImage(userId, imageUri)
                }
                _updateProfileImageStatus.value = false
            } catch (e: Exception) {
                _updateProfileImageStatus.value = false
            }
        }
    }

    fun updateProfileImages(userId: Long, imageUri: String?) {
        viewModelScope.launch {

                userRepository.updateUserProfileImage(userId, imageUri)

        }
    }

    fun getUserImageUri(userId: String): LiveData<String?> {
        return userRepository.getProfileImagePaths(userId)
    }
    fun setImageUri(uri: String) {
        _imageUri.value = uri
    }

    suspend fun getId(user : String ) : Long {
        return userRepository.getUserIdd(user)
    }


    @Suppress("unused")
        constructor() : this(UserRepository()) { }

    // Méthode pour signaler que la navigation a été effectuée
    fun onUserFragmentNavigated() {
        Log.d("ProfilViewModel", "UserFragment navigation completed")
        _navigateToUserFragment.value = false
    }
}



