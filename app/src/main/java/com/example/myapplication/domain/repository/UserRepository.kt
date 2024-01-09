package com.example.myapplication.domain.repository

import android.app.AlertDialog
import com.example.myapplication.model.User
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

import android.content.Context
import dataBase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: DaoUser) {

   // val userDao = AppDatabase.getDatabase().userDao()
    suspend fun registerUser2(user: User) {
        userDao.insert(user)
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
}
