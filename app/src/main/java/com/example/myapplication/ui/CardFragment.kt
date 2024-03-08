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
import com.example.myapplication.model.ViewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardFragment : Fragment() {

    private val cardViewModel: CardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CardAdaptater
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var intentFilter: IntentFilter
    private lateinit var binding: FragmentCardBinding
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

        progressBar = rootView.findViewById(R.id.progressBar)

        cardViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        adapter = CardAdaptater()
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

        adapter.setOnItemClickListener(object : CardAdaptater.OnItemClickListener {
            override fun onItemClick(card: Card) {
                sharedViewModel.selectCard(card)
               findNavController().navigate(R.id.action_cardFragment_to_cardDetailsFragment)
            }
        })

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