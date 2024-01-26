package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.domain.repository.DaoUser
import com.example.myapplication.model.User
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.example.myapplication.model.ViewModel.SignUpViewModel
import dataBase.AppDatabase
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var etNewUsername: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var userDao: DaoUser

    private val signUpViewModel: SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etNewUsername = view.findViewById(R.id.etNewUsername)
        etNewPassword = view.findViewById(R.id.etNewPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        val appDatabase = AppDatabase.getDatabase()
        //val appDatabase = AppDatabase.getDatabasee(requireContext().applicationContext)
        userDao = appDatabase.userDao()

        btnSignUp.setOnClickListener {
            val newUsername = etNewUsername.text.toString()
            val newPassword = etNewPassword.text.toString()

            val newUser = User(username = newUsername, password = newPassword)

            // Insérer le nouvel utilisateur dans la base de données
            lifecycleScope.launch {

                val userId = signUpViewModel.signUp(newUsername, newPassword)
                if (userId > 0) {
                    // L'utilisateur a été créé avec succès, et userId contient l'ID généré
                    Toast.makeText(requireContext(), "Compte créé avec succès (ID: $userId)", Toast.LENGTH_SHORT).show()
                } else {
                    // Une erreur s'est produite lors de la création de l'utilisateur
                    Toast.makeText(requireContext(), "Erreur création compte", Toast.LENGTH_SHORT).show()
                }
//                val existingUser = userDao.getUserByUsernameT(newUser.username)
//                if (existingUser == null) {
//                    // Le nom d'utilisateur n'existe pas, vous pouvez insérer le nouvel utilisateur
//                    userDao.insertUser(newUser)
//                    Toast.makeText(requireContext(), "Compte créé avec succès", Toast.LENGTH_SHORT).show()
//                } else {
//                    // Le nom d'utilisateur existe déjà, vous pouvez générer une erreur ou effectuer une autre action
//                    Toast.makeText(requireContext(), "Erreur creation compte", Toast.LENGTH_SHORT).show()
//                }

            }
        }

        return view
    }
}
