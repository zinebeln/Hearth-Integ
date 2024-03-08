package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import dataBase.AppDatabase
//import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordFragment : Fragment() {
    private lateinit var authViewModel: AuthManager
    private lateinit var userRepository: UserRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        authViewModel = AuthManager(requireContext())
        userRepository = UserRepository()

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
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)

        btnSavePassword.setOnClickListener {

            val oldPassword = etOldPassword.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(context, "Aucun des champs ne peut être vide", Toast.LENGTH_SHORT).show()
                }
                newPassword != confirmPassword -> {
                    Toast.makeText(context, "Le nouveau mot de passe et la confirmation ne correspondent pas", Toast.LENGTH_SHORT).show()
                }
                newPassword.length < 6 -> {
                    Toast.makeText(context, "Le nouveau mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    lifecycleScope.launch {
                        if (isOldPasswordCorrect(oldPassword)) {
                            updatePasswordInDatabase(newPassword)
                            Toast.makeText(context, "Le mot de passe a été modifié avec succès", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_change_password_to_profil_fragment)
                        } else {
                            Toast.makeText(context, "Le mot de passe actuel est incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private suspend fun isOldPasswordCorrect(oldPassword: String): Boolean {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""

        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByUsername(username)
            Log.d("ChangePassword", "test ${user}")
             user?.password == oldPassword
        }
    }

    private suspend fun updatePasswordInDatabase(newPassword: String) {

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""

        withContext(Dispatchers.IO) {
            val userDao = AppDatabase.getDatabase().userDao()
            val user = userRepository.getUserByUsername(username)
            user?.let {
                it.password = newPassword
                userDao.updateUser(it)
            }

        }
    }
}
