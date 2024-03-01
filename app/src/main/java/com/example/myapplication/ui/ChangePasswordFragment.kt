package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import dataBase.AppDatabase
//import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {
    private lateinit var authViewModel: AuthManager
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        authViewModel = AuthManager(requireContext())

        lifecycleScope.launch {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val user = sharedPref.getString("username", "")
//            val userId = sharedPref.getLong("userId", 0)
            val userId = user?.let { userRepository.getUserId(it) }
            Log.d("shared","test userid "+ userId)

        }
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSavePassword = view.findViewById<Button>(R.id.btnSavePassword)
        val etOldPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)

        btnSavePassword.setOnClickListener {
            val oldPassword = etOldPassword.text.toString()
            val newPassword = etNewPassword.text.toString()

            // Vérifier si l'ancien mot de passe est correct
            if (isOldPasswordCorrect(oldPassword)) {
                // Mettre à jour le mot de passe dans la base de données
                updatePasswordInDatabase(newPassword)
                Log.d("ChangePassword", " modif password")
            } else {
                Log.d("ChangePassword", "erreur modif password")
            }
        }
    }

    private fun isOldPasswordCorrect(oldPassword: String): Boolean {
//        val userDao = AppDatabase.getDatabase().userDao()
//        val currentUser = userDao.getCurrentUser()
//        return currentUser != null && currentUser.password == oldPassword

        var isCorrect = true
        GlobalScope.launch(Dispatchers.IO) {
            val userDao = AppDatabase.getDatabase().userDao()
            val currentUser = userDao.getCurrentUser()
            isCorrect = currentUser != null && currentUser.password == oldPassword
        }
        return isCorrect
    }

    private fun updatePasswordInDatabase(newPassword: String) {


//
//        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        // Lorsque l'utilisateur se connecte
//        val userId = sharedPref.getLong("password", 0 )
//        Log.d("ChangePassword", "userid"+ sharedPref.getLong("password", 0) + "user "+ sharedPref.getString("username", "") )
//        if (userId != null) {
//            userDao.updateUserLoggedInStatus(userId.toInt(), isLoggedIn = true)
//        }
////      val u = AppDatabase.initDatabase(requireContext())
//        GlobalScope.launch(Dispatchers.IO) {
//
//            //userDao.updateUserLoggedInStatus(userId, isLoggedIn = false)
//
//            val userDao = AppDatabase.getDatabase().userDao()
//            val currentUser = userDao.getCurrentUser()
//            currentUser?.let {
//                it.password = newPassword
//                userDao.updateUser(it)
//            }
//        }

        lifecycleScope.launch {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val user = sharedPref.getString("username", "")
//            val userId = sharedPref.getLong("userId", 0)
            val userId = user?.let { userRepository.getUserId(it) }
            Log.d("shared","test userid "+ userId)

            if (userId != null) {
                // Utiliser une coroutine pour exécuter les opérations de base de données sur un thread de fond
                GlobalScope.launch(Dispatchers.IO) {
                    val userDao = AppDatabase.getDatabase().userDao()
                    userDao.updateUserLoggedInStatus(userId, true)
                    val currentUser = userDao.getCurrentUser()

                        // Mettre à jour le mot de passe de l'utilisateur
                    currentUser?.let {
                        it.password = newPassword
                        userDao.updateUser(it)
                    }
                }
            } else {
                // Gérer l'erreur si userId est la valeur par défaut
                // Par exemple, afficher un message à l'utilisateur
            }

        }


//       authViewModel = AuthManager(requireContext())
//        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        val userId = sharedPref.getLong("userId", 0)
//        Log.d("ChangePassword", "userid"+ sharedPref.getLong("userId", 0) + "user "+ sharedPref.getString("username", "") )
//
//
//        // Vérifier si userId est différent de la valeur par défaut
//        if (userId.toInt() != 0) {
//            // Utiliser une coroutine pour exécuter les opérations de base de données sur un thread de fond
//            GlobalScope.launch(Dispatchers.IO) {
//                val userDao = AppDatabase.getDatabase().userDao()
//                val currentUser = userDao.getCurrentUser()
//
//                // Mettre à jour le mot de passe de l'utilisateur
//                currentUser?.let {
//                    it.password = newPassword
//                    userDao.updateUser(it)
//                }
//            }
//        } else {
//            // Gérer l'erreur si userId est la valeur par défaut
//            // Par exemple, afficher un message à l'utilisateur
//        }





    }
}
