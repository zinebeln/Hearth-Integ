package com.example.myapplication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Card


import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.model.ViewModel.SharedViewModel


class CardAdaptater : ListAdapter<Card, CardAdaptater.ViewHolder>(CardDiffCallback()) {

    private var onItem: OnItemClickListener? = null
    private lateinit var sharedViewModel: SharedViewModel

    private val imageNamess =
        listOf("imagun", "imagedeux", "imagetrois", "imagecinq", "imagesix")

//    private lateinit var onI: SharedViewModel

    interface OnItemClickListener {
        fun onItemClick(card: Card)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItem = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardName: TextView = itemView.findViewById(R.id.textViewName)
        val cardType: TextView = itemView.findViewById(R.id.textViewType)
        val imageCard: ImageView = itemView.findViewById(R.id.imageViewCard)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val card = getItem(position)
                    onItem?.onItemClick(card)
                    //listener?.onItemClick(card)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val card = getItem(position)
        holder.cardName.text = card.name
        holder.cardType.text = card.type


        if (card.img != null) {
            Glide.with(holder.itemView)
                .load(card.img)
                .apply(RequestOptions().centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageCard)
        } else {

            val randomImageName = imageNamess.random()
            val resourceId = holder.itemView.resources.getIdentifier(
                randomImageName,
                "drawable",
                holder.itemView.context.packageName
            )


            Glide.with(holder.itemView)
                .load(resourceId)
                .apply(RequestOptions().centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageCard)


        }



    }

}

class CardDiffCallback : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.cardId == newItem.cardId // Assurez-vous que vous avez un moyen unique d'identifier les éléments
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}



