package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardAdaptater(private val cardList: List<String>) : RecyclerView.Adapter<CardAdaptater.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Définissez les éléments de la mise en page de la carte ici (par exemple, TextView, ImageView, etc.)
        val cardName: TextView = itemView.findViewById(R.id.textViewName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Créez une vue pour chaque élément de la liste (appelée lorsque la RecyclerView a besoin d'une nouvelle vue)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Liez les données de la carte à la vue (appelée pour chaque élément de la liste)
        val card = cardList[position]
        holder.cardName.text = card.toString()
        // Mettez à jour la vue avec les données de la carte
    }
    override fun getItemCount(): Int {
        // Retournez le nombre d'éléments dans la liste
        return cardList.size
    }
}
