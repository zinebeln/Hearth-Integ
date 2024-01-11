package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "Card")
data class Card(
    @PrimaryKey val cardId: String = "popo",
    val dbfId: Int,
    val name: String,
    val cardSet: String,
    val type: String,
    val text: String?,
    val playerClass: String,
    val locale: String,
    val cost: Int?,
    val attack: Int?,
    val health: Int?,
    val flavor: String?,
    val faction: String?,
    val rarity: String?,
    @ColumnInfo(name = "img") val img: String?,
//    val mechanics: List<Mechanic>?,
    val spellSchool: String?,
    val race: String?,
    val elite: Boolean?,
    var isFavorite: Boolean = false
) : Serializable
@Entity
data class Mechanic(
    val name: String
)
@Entity
data class Cards(val Basic: List<Card> = emptyList())

