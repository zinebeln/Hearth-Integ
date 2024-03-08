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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.DecksCard
class DecksAdapter(private val listener: OnItemClickListener) : ListAdapter<DecksCard, DecksAdapter.ViewHolder>(DecksCardDiffCallback()) {
    interface OnItemClickListener {
        fun onItemClicked(decksCard: DecksCard)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_decks_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deckCard = getItem(position)
        holder.bind(deckCard)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardName: TextView = itemView.findViewById(R.id.decksTextName)
        private val cardType: TextView = itemView.findViewById(R.id.decksTextType)
        private val imageViewCard: ImageView = itemView.findViewById(R.id.decksImageView)
        private val imageNamess =
            listOf("imagun", "imagedeux", "imagetrois", "imagecinq", "imagesix")

        fun bind(decksCard: DecksCard) {
            cardName.text = decksCard.card?.name
            cardType.text = decksCard.card?.type
            Glide.with(itemView).load(decksCard.card?.img).into(imageViewCard)

            if (decksCard.card.img != null) {
                Glide.with(itemView)
                    .load(decksCard.card.img )
                    .apply(RequestOptions().centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageViewCard)
            } else {
                val randomImageName = imageNamess.random()
                val resourceId = itemView.resources.getIdentifier(
                    randomImageName,
                    "drawable",
                    itemView.context.packageName
                )
                Glide.with(itemView)
                    .load(resourceId)
                    .apply(RequestOptions().centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageViewCard)
            }
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
        return oldItem.cardsId == newItem.cardsId
    }
    override fun areContentsTheSame(oldItem: DecksCard, newItem: DecksCard): Boolean {
        return oldItem == newItem
    }
}
