package com.example.myapplication.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CardAdaptater
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.model.Card
import com.example.myapplication.ui.adaptater.AdapterCards


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Déclarez la RecyclerView en tant que membre de classe
    private lateinit var recyclerView: RecyclerView

    private lateinit var cardDao: CardDataService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)

        // Initialiser le DAO
//        val db = (requireActivity().application as MainActivity).db
//        cardDao = db.cardDao()
    }

    private fun getCards() {
//        val cards: List<Card> = cardDao.getCardsData()

        // Maintenant, 'cards' contient la liste des cartes récupérées depuis la base de données
        // Utilisez cette liste pour afficher les cartes dans votre RecyclerView, par exemple
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {

              //  requireActivity().onBackPressed()
                parentFragmentManager.popBackStack() // Cela reviendra à la page précédente
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        // Initialisez la RecyclerView ici
        recyclerView = view.findViewById(R.id.cardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Récupérer les données de la carte depuis les arguments du fragment
        val card: Card? = arguments?.getSerializable("CARD_DATA") as? Card

        // Afficher les données de la carte dans les composants d'interface utilisateur (par exemple, TextView)
       // view.findViewById<TextView>(R.id.textViewCardName).text = card?.name
        // view.findViewById<TextView>(R.id.textViewCardType).text = card?.type
       // recyclerView.adapter = CardAdaptater(cards) // Ici, "cards" est la liste de cartes que vous souhaitez afficher
       // recyclerView.adapter = AdapterCards(cards)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false)
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
//        fun newInstance(param1: String, param2: String) =
//            CardFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
        fun newInstance(card: Card): CardFragment {
            val fragment = CardFragment()
            val args = Bundle()
            args.putSerializable("CARD_DATA", card)
            fragment.arguments = args
            return fragment
        }
    }
}