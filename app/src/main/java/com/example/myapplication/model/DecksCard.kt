package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks_cards")
data class DecksCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: String,
    val card : Card

)