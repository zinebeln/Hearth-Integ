package com.example.myapplication.ui.adaptater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.CardsList

class AdapterCards (private val cards: CardsList) : RecyclerView.Adapter<AdapterCards.ViewHolder>() {
    // Implémentez les méthodes nécessaires de l'adaptateur, notamment onCreateViewHolder et onBindViewHolder

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Déclarez et initialisez les vues de l'élément de liste ici
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        // Initialisez d'autres vues de l'élément de liste ici
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        return cards.cardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
        val c = cards[position]
        holder.textViewName.text = cards.get(position)?.cardId;
        // Définissez les autres vues de l'élément de liste ici
    }
}