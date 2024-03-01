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

import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.domain.repository.UserViewModelFactory
import com.example.myapplication.model.User
import com.example.myapplication.model.ViewModel.AuthViewModel
import com.example.myapplication.model.ViewModel.UserViewModel
import dataBase.AppDatabase
import kotlinx.coroutines.launch


class UserFragment : Fragment() {

    private lateinit var userRepository: UserRepository
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCreateAccount: Button
    private lateinit var auth: AuthManager
   // private lateinit var userRepository: UserRepository
    val userDao = AppDatabase.getDatabase().userDao()

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        // Initialiser userRepository ici
//        userRepository = UserRepository()
//    }
    private val authViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        userRepository = UserRepository()


        authViewModel.userLoggedIn.observe(viewLifecycleOwner, Observer { userLoggedIn ->

            Log.d("UserFragment", "User logged in: $userLoggedIn")
            if (userLoggedIn) {

                Toast.makeText(requireContext(), "Utilisateur connecté avec succès", Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(requireContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show()
            }
        })

        // Appeler la fonction pour créer et vérifier la connexion de l'utilisateur "tutu"
       // authViewModel.registerAndLoginUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        auth = AuthManager(requireContext())
       // auth = AuthManager(userRepository)
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount)
        //userRepository = UserRepository()
        userRepository = UserRepository()

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            authViewModel.login(username, password) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(requireContext(), "Connexion réussie", Toast.LENGTH_SHORT).show()
                    val authManager = AuthManager(requireContext())
                   // val authManager = AuthManager(userRepository)
                    authManager.setLoggedIn(true)
//                    lifecycleScope.launch {
//                        authManager.setLoggedIn(true)
//                    }

                    // Mettre à jour le statut isLoggedIn dans la base de données

                    val newUserr= User(username = username, password = password, isLoggedIn = true)
                    val newUser = User(username = username, password = password)
                    auth.setUser(newUserr)

                    lifecycleScope.launch {
                        // Mettre à jour le statut isLoggedIn dans la base de données
                        userRepository.updateUserLoggedInStatus(newUserr.userId, true)
                        Log.d("UserFragment", "Mise à jour du statut isLoggedIn effectuée pour l'utilisateur : $newUserr")
                    }


//                    // authViewModel.updateUserLoggedInStatus(true)
//                    lifecycleScope.launch {
//                        userRepository.updateUserLoggedInStatus(newUserr)
//                        Log.d("UserFragment", "a" + newUserr)
//
//                    }
//                    userRepository.updateUserLoggedInStatus(newUserr)
//                    lifecycleScope.launch {
//                        auth.setUser(newUser)
//                    }
                    // Lorsque l'utilisateur se connecte
//                    authViewModel.updateUserLoginStatus(username, true)

                    Log.d("UserFragment", "Avant la navigation vers CardFragment")





                    // Stocker l'identifiant dans les préférences partagées
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

//                    val loggedInUser = authViewModel.getLoggedInUser()

                    findNavController().navigate(R.id.action_userFragment_to_cardFragment)
                    Log.d("UserFragment", "Après la navigation vers CardFragment")

                } else {
                    Toast.makeText(requireContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnCreateAccount.setOnClickListener {
//            val username = etUsername.text.toString()
//            val password = etPassword.text.toString()
//
//            val newUser = User(username = username, password = password)
//
//            lifecycleScope.launch {
//                userDao.insertUser(newUser)
//                Toast.makeText(requireContext(), "Compte créé avec succès", Toast.LENGTH_SHORT).show()
//            }

            findNavController().navigate(R.id.action_userFragment_to_signUpFragment)


           // authViewModel.register(username, password)
        }
        return view
    }



}