package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.DecksCard

// FavoriteAdapter.kt

//class DecksAdapter : ListAdapter<DecksCard, DecksAdapter.ViewHolder>(DecksCardDiffCallback()) {
//class DecksAdapter(private val decksCards: List<DecksCard>) : RecyclerView.Adapter<DecksAdapter.ViewHolder>() {
//

//class DecksAdapter : ListAdapter<DecksCard, DecksAdapter.ViewHolder>(DecksCardDiffCallback()) {
class DecksAdapter(private val listener: OnItemClickListener) : ListAdapter<DecksCard, DecksAdapter.ViewHolder>(DecksCardDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClicked(decksCard: DecksCard)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_decks_card, parent, false)
//        return ViewHolder(itemView)
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_decks_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val favoriteCard = getItem(position)
//        holder.bind(favoriteCard)
        val deckCard = getItem(position)
        // Mettez à jour le contenu de votre élément de liste (par exemple, un TextView) avec les détails de la carte.
        holder.bind(deckCard)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardName: TextView = itemView.findViewById(R.id.decksTextName)
        private val cardType: TextView = itemView.findViewById(R.id.decksTextType)
        private val imageViewCard: ImageView = itemView.findViewById(R.id.decksImageView)

        fun bind(decksCard: DecksCard) {

            cardName.text = decksCard.card?.name
            cardType.text = decksCard.card?.type

            Glide.with(itemView).load(decksCard.card?.img).into(imageViewCard)
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val decksCard = getItem(position)
                    listener.onItemClicked(decksCard)
                }
            }
        }


    }
}
class DecksCardDiffCallback : DiffUtil.ItemCallback<DecksCard>() {
    override fun areItemsTheSame(oldItem: DecksCard, newItem: DecksCard): Boolean {
        // Retourne true si les identifiants des éléments sont les mêmes
        return oldItem.cardId == newItem.cardId
    }

    override fun areContentsTheSame(oldItem: DecksCard, newItem: DecksCard): Boolean {
        // Retourne true si les contenus des éléments sont les mêmes
        return oldItem == newItem
    }
}
