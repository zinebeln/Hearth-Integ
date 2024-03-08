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
import androidx.navigation.fragment.findNavController
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
    private lateinit var etNewConfirmPassword: EditText
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
        etNewConfirmPassword = view.findViewById(R.id.etNewConfirmPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        val appDatabase = AppDatabase.getDatabase()
        userDao = appDatabase.userDao()

        btnSignUp.setOnClickListener {
            val newUsername = etNewUsername.text.toString()
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etNewConfirmPassword.text.toString()

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), "Le mot de passe et le mot de passe de confirmation ne sont pas identiques", Toast.LENGTH_SHORT).show()
            } else {
                val newUser = User(username = newUsername, password = newPassword, profileImagePath = "")
                lifecycleScope.launch {
                    if (!signUpViewModel.isUsernameAvailable(newUsername)) {
                        Toast.makeText(requireContext(), "Votre pseudo est deja utilisé, veuillez le changer", Toast.LENGTH_SHORT).show()
                    } else {
                        val userId = signUpViewModel.signUp(newUsername, newPassword)
                        if (userId > 0) {
                            Toast.makeText(
                                requireContext(),
                                "Compte créé avec succès (ID: $userId)",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_signUpFragment_to_userFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Erreur création compte",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        return view
    }
}
