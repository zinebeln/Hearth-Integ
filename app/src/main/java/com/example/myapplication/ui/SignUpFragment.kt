package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.domain.repository.DaoUser
import com.example.myapplication.model.User
import dataBase.AppDatabase
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var etNewUsername: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var userDao: DaoUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etNewUsername = view.findViewById(R.id.etNewUsername)
        etNewPassword = view.findViewById(R.id.etNewPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        val appDatabase = AppDatabase.getDatabase()
        userDao = appDatabase.userDao()

        btnSignUp.setOnClickListener {
            val newUsername = etNewUsername.text.toString()
            val newPassword = etNewPassword.text.toString()

            val newUser = User(username = newUsername, password = newPassword)

            // Insérer le nouvel utilisateur dans la base de données
            lifecycleScope.launch {
                userDao.insertUser(newUser)
                Toast.makeText(requireContext(), "Compte créé avec succès", Toast.LENGTH_SHORT).show()

                // Naviguer vers une autre destination ou effectuer d'autres actions nécessaires
            }
        }

        return view
    }
}
