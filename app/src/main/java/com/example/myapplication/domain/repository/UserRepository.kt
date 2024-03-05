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

class UserRepository() {

   private val userDao = AppDatabase.getDatabase().userDao()
    suspend fun registerUser2(user: User) {
        userDao.insert(user)
    }
    suspend fun getUserId(username : String): Long? {
       return userDao.getUserId(username)
    }
    suspend fun getUserIdd(username : String): Long {
        return userDao.getUserIdd(username)
    }
    suspend fun isUsernameAvailable(username: String): Boolean {
        val existingUser = userDao.getUserByUsername(username)
        return existingUser == null
    }
    suspend fun registerUserr(user: User) {
        userDao.insert(user)
    }
    suspend fun updateUserLoggedInStatus(userId: Long, isLoggedIn: Boolean) {
        userDao.updateUserLoggedInStatus(userId, isLoggedIn)
    }

    suspend fun getUserIdd(user: User) {
        userDao.getCurrentUserIdd(user.username)
    }
    suspend fun registerUser(user: User) {
        val existingUser = userDao.getUserByUsernameT(user.username)
        if (existingUser == null) {
            userDao.insert(user)
        } else {

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
        withContext(Dispatchers.IO) {
            userDao.delete(user)
        }

    }

    suspend fun getProfileImagePath(username : String): String ?{
        return userDao.getImgPath(username)
    }

    suspend fun updateUserProfileImage(userId: Long, imagePath: String) {
        withContext(Dispatchers.IO) {
            userDao.updateUserProfileImage(userId, imagePath)
        }
    }

}
