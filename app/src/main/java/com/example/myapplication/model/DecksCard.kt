package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



//

//    foreignKeys = [
//    ForeignKey(
//        entity = User::class,
//        parentColumns = ["id"],
//        childColumns = ["userId"],
//        onDelete = ForeignKey.CASCADE
//    )
//])

@Entity(tableName = "decks_cards")
data class DecksCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: String,
    val card : Card

)