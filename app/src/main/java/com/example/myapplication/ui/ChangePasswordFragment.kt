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

            if (isOldPasswordCorrect(oldPassword)) {
                updatePasswordInDatabase(newPassword)
                Log.d("ChangePassword", " modif password")
            } else {
                Log.d("ChangePassword", "erreur modif password")
            }
        }
    }

    private fun isOldPasswordCorrect(oldPassword: String): Boolean {
        var isCorrect = true
        GlobalScope.launch(Dispatchers.IO) {
            val userDao = AppDatabase.getDatabase().userDao()
            val currentUser = userDao.getCurrentUser()
            isCorrect = currentUser != null && currentUser.password == oldPassword
        }
        return isCorrect
    }

    private fun updatePasswordInDatabase(newPassword: String) {

        lifecycleScope.launch {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val user = sharedPref.getString("username", "")
            val userId = user?.let { userRepository.getUserId(it) }
            Log.d("shared","test userid "+ userId)

            if (userId != null) {
               GlobalScope.launch(Dispatchers.IO) {
                    val userDao = AppDatabase.getDatabase().userDao()
                    userDao.updateUserLoggedInStatus(userId, true)
                    val currentUser = userDao.getCurrentUser()
                     currentUser?.let {
                        it.password = newPassword
                        userDao.updateUser(it)
                    }
                }
            } else {
                // Gérer l'erreur si userId est la valeur par défaut
            }
        }
    }
}
