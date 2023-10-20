package com.example.myapplication.ui.adaptater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.CardsList

class CardAdapter(private val cards: CardsList) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    // Implémentez les méthodes nécessaires de l'adaptateur, notamment onCreateViewHolder et onBindViewHolder

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Déclarez et initialisez les vues de l'élément de liste ici
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
