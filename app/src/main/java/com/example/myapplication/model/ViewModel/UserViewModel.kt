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


    fun updateUserLoggedInStatus(isLoggedIn: Boolean) {
        _userLoggedIn.value = isLoggedIn
    }

//    val userDao = AppDatabase.getDatabase().userDao()
//    private val userRepositoryy = UserRepository(userDao)

//    @Suppress("unused")
//    constructor() : this(UserRepository(userDao)) {
//    }

    fun registerAndLoginUser() {
        viewModelScope.launch {

            val userToRegister = User(username = "tutu", password = "mot_de_passe", profileImagePath = "")
            userRepository.registerUser(userToRegister)
            val loggedInUser = userRepository.getUserByUsername("tutu")
            _userLoggedIn.value = loggedInUser != null
        }
    }

    fun register(username: String, password: String) {
        val user = User(username = username, password = password, profileImagePath = "")
        viewModelScope.launch {
            userRepository.registerUser(user)
        }
    }

//    suspend fun registerUser(user: User) {
//        val existingUser = userDao.getUserByUsernameT(user.username)
//        if (existingUser == null) {
//            userDao.insert(user)
//        } else {
//            // Gérer le cas où le nom d'utilisateur existe déjà
//            // Vous pouvez lancer une exception, afficher un message d'erreur, etc.
//        }
//    }

    fun login(username: String, password: String, onLoginResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByUsername(username)
            val isLoginSuccessful = user?.password == password
            onLoginResult(isLoginSuccessful)


        }
    }

//   fun updateUserLoginStatus(username: String, isLoggedIn: Boolean) {
//       viewModelScope.launch {
//           userRepository.updateUserLoginStatus(username, isLoggedIn)
//       }
//    }

//     fun getLoggedInUser() {
//
//         viewModelScope.launch {
//            val user = userRepository.getLoggedInUser()
//         }
//
//    }
}