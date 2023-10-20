package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CardsList (@SerializedName("CardsList") val cardList: List<Card> ) {
}
