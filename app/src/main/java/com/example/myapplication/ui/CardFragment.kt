package com.example.myapplication.ui


import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
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
import com.example.myapplication.model.ViewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardFragment : Fragment() {

    private val cardViewModel: CardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CardAdaptater
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var intentFilter: IntentFilter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("CardFragment", "onCreateView called")
        val rootView = inflater.inflate(R.layout.fragment_card, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.cardRecyclerView)

        progressBar = rootView.findViewById(R.id.progressBar)
        //val prog = rootView.findViewById<ProgressBar>(R.id.progressBar)
        cardViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        adapter = CardAdaptater()
        recyclerView.adapter = adapter
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2 colonnes, ajustez selon vos besoins
        }

        cardViewModel.cardsList.observe(viewLifecycleOwner, Observer { cards ->
            Log.d("CardFragment", "observer $cards")
            adapter.submitList(cards)
        })

        val offlineTextView = rootView.findViewById<TextView>(R.id.offlineTextView)

        if (isNetworkAvailable()) {
            lifecycleScope.launch {
                cardViewModel.fetchCards()
            }
        } else {
            offlineTextView.visibility = View.VISIBLE
            cardViewModel.loadCardsFromDatabase()
        }
        networkChangeReceiver = NetworkChangeReceiver()
        intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(networkChangeReceiver, intentFilter)

//        lifecycleScope.launch {
//            cardViewModel.fetchCards2()
//        }

        adapter.setOnItemClickListener(object : CardAdaptater.OnItemClickListener {
            override fun onItemClick(card: Card) {
                sharedViewModel.selectCard(card)
               // val action = CardFragmentDirections.actionCardFragmentToCardDetailsFragment(cardid = card.cardId)
                findNavController().navigate(R.id.action_cardFragment_to_cardDetailsFragment)
            }
        })




        val bottomNavigationView = rootView.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)

        return rootView
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(card: Card?): CardFragment {
            val fragment = CardFragment()
            val args = Bundle()
            args.putSerializable("CARD_DATA", card)
            fragment.arguments = args
            return fragment
        }
    }
}