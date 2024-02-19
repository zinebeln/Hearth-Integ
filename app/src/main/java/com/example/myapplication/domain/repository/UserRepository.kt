package com.example.myapplication.domain.repository

import android.app.AlertDialog
import com.example.myapplication.model.User
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import com.example.myapplication.ui.AuthManager
import dataBase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//class UserRepository(private val userDao: DaoUser) {
class UserRepository() {

   val userDao = AppDatabase.getDatabase().userDao()

//    val loggedInUser: LiveData<User> = userDao.getLoggedInUserLiveData()

    suspend fun registerUser2(user: User) {
        userDao.insert(user)
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        // Vérifiez si le nom d'utilisateur existe déjà dans la base de données
        val existingUser = userDao.getUserByUsername(username)
        return existingUser == null // Renvoie true si le nom d'utilisateur est disponible, sinon false
    }
    suspend fun registerUserr(user: User) {

            userDao.insert(user)

    }

    suspend fun updateUserLoggedInStatus(userId: Long, isLoggedIn: Boolean) {
        userDao.updateUserLoggedInStatus(userId, isLoggedIn)
    }

//    suspend fun getUserId(user: User) {
//        userDao.getCurrentUserId(user.userId)
//    }
    suspend fun getUserIdd(user: User) {
        userDao.getCurrentUserIdd(user.username)
    }
    suspend fun registerUser(user: User) {
        val existingUser = userDao.getUserByUsernameT(user.username)
        if (existingUser == null) {
            userDao.insert(user)
        } else {
            // Gérer le cas où le nom d'utilisateur existe déjà

           // showUsernameExistsPopup(requireContext())
        }
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertt(user)
    }

     fun showUsernameExistsPopup(context : Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Erreur d'inscription")
            .setMessage("Le nom d'utilisateur existe déjà. Veuillez en choisir un autre.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsernameT(username)
    }

    suspend fun deleteUser(user: User) {
        // Utilisez un contexte approprié (par exemple, avec viewModelScope pour un ViewModel)
        withContext(Dispatchers.IO) {
            userDao.delete(user)
        }

    }

    //changement ici

//    suspend fun updateUserLoginStatus(username: String?, isLoggedIn: Boolean) {
//        withContext(Dispatchers.IO) {
//            userDao.updateUserLoginStatus(username, isLoggedIn)
//        }
//    }

//    suspend fun getLoggedInUser(): User? {
//        return withContext(Dispatchers.IO) {
//            userDao.getLoggedInUser()
//        }
//    }

//    suspend fun getLoggedInUserId(): String? {
//        return userDao.getLoggedInUserId()
//    }

//    suspend fun isUserLoggedIn(): Boolean {
//        val loggedInUser = userDao.getLoggedInUser()
//        return loggedInUser != null
//    }


}
