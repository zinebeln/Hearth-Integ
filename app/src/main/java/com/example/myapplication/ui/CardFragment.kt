package com.example.myapplication.ui


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CardAdaptater
import com.example.myapplication.R
import com.example.myapplication.model.Card
import com.example.myapplication.model.ViewModel.CardViewModel
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardFragment : Fragment() {

    private lateinit var cardAdapter: CardAdaptater
    private val cardViewModel: CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.fragment_menu, menu)
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_back -> {
//                parentFragmentManager.popBackStack()
//                return true
//            }
//           // else -> return super.onOptionsItemSelected(item)
//        }
//
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("CardFragment", "onCreateView called")
        val rootView = inflater.inflate(R.layout.fragment_card, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.cardRecyclerView)
        val prog = rootView.findViewById<ProgressBar>(R.id.progressBar)

        cardAdapter = CardAdaptater()
        recyclerView.adapter = cardAdapter
       // recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2 colonnes, ajustez selon vos besoins
           // adapter = cardAdapter
        }


        cardViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                prog.visibility = View.VISIBLE
            } else {
                prog.visibility = View.GONE
            }
        })


        // Supprimez cet observer
        cardViewModel.cardsList.observe(viewLifecycleOwner, Observer { cards ->
            cardAdapter.submitList(cards)
        })


        lifecycleScope.launch {
            cardViewModel.fetchCards2()
        }
        // Initialisation de la navigation avec la BottomNavigationView
        val bottomNavigationView = rootView.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)


        return rootView


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