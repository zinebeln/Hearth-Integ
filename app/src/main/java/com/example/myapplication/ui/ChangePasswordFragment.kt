package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import dataBase.AppDatabase
//import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSavePassword = view.findViewById<Button>(R.id.btnSavePassword)
        val etOldPassword = view.findViewById<Button>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<Button>(R.id.etNewPassword)

        btnSavePassword.setOnClickListener {
            val oldPassword = etOldPassword.text.toString()
            val newPassword = etNewPassword.text.toString()

            // Vérifier si l'ancien mot de passe est correct
            if (isOldPasswordCorrect(oldPassword)) {
                // Mettre à jour le mot de passe dans la base de données
                updatePasswordInDatabase(newPassword)
            } else {
                Log.d("ChangePassword", "erreur modif password")
            }
        }
    }

    private fun isOldPasswordCorrect(oldPassword: String): Boolean {
        val userDao = AppDatabase.getDatabase().userDao()
        val currentUser = userDao.getCurrentUser()
        return currentUser != null && currentUser.password == oldPassword
    }

    private fun updatePasswordInDatabase(newPassword: String) {

//      val u = AppDatabase.initDatabase(requireContext())
        GlobalScope.launch(Dispatchers.IO) {
            val userDao = AppDatabase.getDatabase().userDao()
            val currentUser = userDao.getCurrentUser()
            currentUser?.let {
                it.password = newPassword
                userDao.updateUser(it)
            }
        }
    }
}
