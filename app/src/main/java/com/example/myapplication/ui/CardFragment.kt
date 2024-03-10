package com.example.myapplication.ui


import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CardAdaptater
import com.example.myapplication.R
import com.example.myapplication.model.Card
import com.example.myapplication.model.ViewModel.CardViewModel
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.FragmentCardBinding
import com.example.myapplication.domain.repository.DecksCardRepository
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.example.myapplication.model.ViewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardFragment : Fragment() {

    private val cardViewModel: CardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val decksViewModel: DecksViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CardAdaptater
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var intentFilter: IntentFilter
    private lateinit var binding: FragmentCardBinding
    private lateinit var userRepository: UserRepository
    private lateinit var decksRepos : DecksCardRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)

        val rootView = inflater.inflate(R.layout.fragment_card, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.cardRecyclerView)
        val editTextSearch = rootView.findViewById<EditText>(R.id.searchEditText)

        userRepository = UserRepository()
        decksRepos = DecksCardRepository()
        progressBar = rootView.findViewById(R.id.progressBar)

        cardViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        adapter = CardAdaptater().apply {
            onStarClick = { card ->
                val position = currentList.indexOf(card)
                if (position != RecyclerView.NO_POSITION) {
                    card.isFavorite = !card.isFavorite
                    val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                    val username = sharedPref.getString("username", "") ?: ""
                    lifecycleScope.launch {
                        val userId = userRepository.getUserId(username)
                        Toast.makeText(requireContext(), "Carte ajoutée aux favoris ", Toast.LENGTH_SHORT).show()
                        notifyItemChanged(position)
                        decksViewModel.toggleFavoriteStatus4(card, userId)
                    }
                }
            }
        }

        recyclerView.adapter = adapter
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        editTextSearch.addTextChangedListener { text ->
            val query = text.toString().trim()
            cardViewModel.searchCards(query)
        }

        cardViewModel.searchResults.observe(viewLifecycleOwner, Observer { results ->
            adapter.submitList(results)
        })

        cardViewModel.cardsList.observe(viewLifecycleOwner, Observer { cards ->
//            val listDeck = decksViewModel.getFavCardID()
//            val filteredCards = cards.filter { !listDeck.contains(it.cardId }
            adapter.submitList(cards)
        })

        if (isNetworkAvailable()) {
            lifecycleScope.launch {
                cardViewModel.fetchCards()
            }
        }
        if(!isNetworkAvailable()){
            cardViewModel.loadCardsFromDatabase()
        }

        networkChangeReceiver = NetworkChangeReceiver()
        intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(networkChangeReceiver, intentFilter)

        val bottomNavigationView = rootView.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewModel.filteredType.observe(viewLifecycleOwner, Observer { results ->
            adapter.submitList(results)
        })
        cardViewModel.filteredAtt.observe(viewLifecycleOwner, Observer { results ->
            adapter.submitList(results)
        })
        cardViewModel.filteredCost.observe(viewLifecycleOwner, Observer { results ->
            adapter.submitList(results)
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_type -> {
                cardViewModel.getCardsSortedByType()
                true
            }
            R.id.action_sort_by_cost -> {
                cardViewModel.getCardsSortedByAttack()
                true
            }
            R.id.action_sort_by_name -> {
                cardViewModel.getCardsSortedByName()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}


