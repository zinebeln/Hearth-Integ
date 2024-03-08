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
    suspend fun updateUserLoggedInStatus(userId: Long, isLoggedIn: Boolean) {
        userDao.updateUserLoggedInStatus(userId, isLoggedIn)
    }

    suspend fun registerUser(user: User) {
        val existingUser = userDao.getUserByUsernameT(user.username)
        if (existingUser == null) {
            userDao.insert(user)
        } else {
            //Message d'erreur
        }
    }
    suspend fun insertUser(user: User): Long {
        return userDao.insertt(user)
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

     fun getProfileImagePaths(username : String): LiveData<String?> {
        return userDao.getImgPaths(username)
    }

    suspend fun updateUserProfileImage(userId: Long, imagePath: String?) {
        withContext(Dispatchers.IO) {
            userDao.updateUserProfileImage(userId, imagePath)
        }
    }

}
