package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User
import com.example.myapplication.model.ViewModel.AuthViewModel
import com.example.myapplication.model.ViewModel.ProfilViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilFragment : Fragment()  {

    private lateinit var textUsername: TextView
    private val viewModel: ProfilViewModel  by viewModels()
    private lateinit var authViewModel: AuthManager
    private lateinit var userObserver: Observer<User>
   // private lateinit var userRepository: UserRepository

//    override fun onDestroyView() {
//        // Retirez l'observation lors de la destruction de la vue
//        authViewModel.user.removeObserver(userObserver)
//        super.onDestroyView()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        authViewModel = AuthManager(requireContext())

//        authViewModel = AuthManager(userRepository)
        textUsername = view.findViewById(R.id.textUsername)

        // Récupérer l'identifiant depuis les préférences partagées
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        textUsername.text = username

      //  viewModel = ViewModelProvider(this).get(ProfilViewModel::class.java)

//        authViewModel.user.observe(viewLifecycleOwner, Observer { user ->
//            Log.d("ProfilFragment", "User observed: $user")
//        })

        val deleteAccountButton = view.findViewById<Button>(R.id.btnDeleteAccount)
        deleteAccountButton.setOnClickListener {
            authViewModel.user.value?.let { user ->
                viewModel.deleteAccount(user)
            } ?: Log.e("ProfilFragment", "User is null")
        }


        // Observer pour détecter le changement de navigation
        viewModel.navigateToUserFragment.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            Log.d("ProfilFragment", "Navigate to UserFragment: $shouldNavigate")
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_profilFragment_to_userFragment)
                // Indiquez au ViewModel que la navigation a été effectuée
                viewModel.onUserFragmentNavigated()
            }
        })

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationProfil)
        val navController = findNavController()
        // Attachez le NavController au BottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//
//                R.id.action_cards -> {
//                    navController.navigate(R.id.action_profilFragment_to_cardFragment)
//                    true
//                }
//                R.id.action_decks -> {
//                    navController.navigate(R.id.action_profilFragment_to_deckFragment)
//                    true
//                }
//
//                R.id.action_profil -> {
//                  //  navController.navigate(R.id.action_cardFragment_to_section3Fragment)
//                    true
//                }
//
//                else -> false
//            }
//        }

        return view
    }
}