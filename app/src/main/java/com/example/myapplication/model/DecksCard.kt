package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
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

@Entity(tableName = "decks_cards", foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = CASCADE)])
data class DecksCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "userId") val userId: Long?,
    @ColumnInfo(name = "cardsId") val cardsId: String,
    @Embedded val card : Card

)
//{
//    // Ajoutez un constructeur sans paramètres
//    constructor() : this(0, 0, "", Card(
//        "",
//        0,
//        "",
//        "",
//        "",
//        null,
//        "",
//        "",
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null
//
//
//    ))
//}

//@Entity(tableName = "decks_cards", primaryKeys = ["id", "cardId"])
//data class DecksCard(
//    val id: Long = 0,
//    val cardId: String,
//    val userId: String,
//    val card : Card
//)
