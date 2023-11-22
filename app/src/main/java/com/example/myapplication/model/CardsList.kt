package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CardsList (@SerializedName("CardsList") val cardList: List<Card> ) {
    operator fun get(position: Int): Card? {
        // Vérifiez si la position est valide
        if (position >= 0 && position < cardList.size) {
            return cardList[position]
        }
        return null // Retournez null si la position est en dehors de la plage valide
    }

}
