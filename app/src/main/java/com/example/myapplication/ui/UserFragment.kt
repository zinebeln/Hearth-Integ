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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.domain.repository.UserViewModelFactory
import com.example.myapplication.model.User
import com.example.myapplication.model.ViewModel.UserViewModel
import kotlinx.coroutines.launch

class UserFragment : Fragment() {

    private lateinit var userRepository: UserRepository
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCreateAccount: Button
    private lateinit var auth: AuthManager

    private val authViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authViewModel.userLoggedIn.observe(viewLifecycleOwner, Observer { userLoggedIn ->

            Log.d("UserFragment", "User logged in: $userLoggedIn")
            if (userLoggedIn) {
                Toast.makeText(requireContext(), "Utilisateur connecté avec succès", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        auth = AuthManager(requireContext())

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount)
        userRepository = UserRepository()

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            authViewModel.login(username, password) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(requireContext(), "Connexion réussie", Toast.LENGTH_SHORT).show()
                    val authManager = AuthManager(requireContext())
                    authManager.setLoggedIn(true)
                    val newUserr= User(username = username, password = password, isLoggedIn = true, profileImagePath = "")
                   // val newUser = User(username = username, password = password, profileImagePath = "")
                    auth.setUser(newUserr)

                    lifecycleScope.launch {
                        userRepository.updateUserLoggedInStatus(newUserr.userId, true)
                        Log.d("UserFragment", "Mise à jour du statut isLoggedIn effectuée pour l'utilisateur : $newUserr")
                    }

                    val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString("username", username)
                        putString("password", password)
                        lifecycleScope.launch {
                            val userId = userRepository.getUserIdd(username)
                            Log.d("shared","test userid "+ userId)
                            putLong("userId", userId)
                        }
                        apply()
                    }
                    findNavController().navigate(R.id.action_userFragment_to_cardFragment)
                } else {
                    Toast.makeText(requireContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_signUpFragment)
        }
        return view
    }

}