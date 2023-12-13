package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


//data class Card (
  //  var patch: String,
    //var classes : String,
    //var sets : String,
    //var standard : String,
    //var wild : String,
    //var types : String,
    //var factions : String,
    //var qualities : String,
    //var races : String,
    //var locales : String
//)

//data class Card
//    (
//    val cardId: String = "popo",
//    val dbfId: Int,
//    val name: String,
//    val cardSet: String,
//    val type: String,
//    val text: String?,
//    val playerClass: String,
//    val locale: String,
//    val cost: Int?,
//    val attack: Int?,
//    val health: Int?,
//    val flavor: String?,
//    val faction: String?,
//    val rarity: String?,
//    val mechanics: List<Mechanic>?,
//    val spellSchool: String?,
//    val race: String?,
//    val elite: Boolean?
//)  : Parcelable {
//
//    // Constructeur principal
//    constructor(parcel: Parcel) : this(
//        // Déparcelable vos champs ici...
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        // Écrire vos champs dans le Parcel
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Card> {
//        override fun createFromParcel(parcel: Parcel): Card {
//            return Card(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Card?> {
//            return arrayOfNulls(size)
//        }
//    }


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
    val elite: Boolean?
) : Serializable

//    : Parcelable {
//
//    // Constructeur principal
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "popo",
//        parcel.readInt(),
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString(),
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.createTypedArrayList(Mechanic),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        // Écrire vos champs dans le Parcel
//        parcel.writeString(cardId)
//        parcel.writeInt(dbfId)
//        parcel.writeString(name)
//        parcel.writeString(cardSet)
//        parcel.writeString(type)
//        parcel.writeString(text)
//        parcel.writeString(playerClass)
//        parcel.writeString(locale)
//        parcel.writeValue(cost)
//        parcel.writeValue(attack)
//        parcel.writeValue(health)
//        parcel.writeString(flavor)
//        parcel.writeString(faction)
//        parcel.writeString(rarity)
//        parcel.writeTypedList(mechanics)
//        parcel.writeString(spellSchool)
//        parcel.writeString(race)
//        parcel.writeValue(elite)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Card> {
//        override fun createFromParcel(parcel: Parcel): Card {
//            return Card(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Card?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
//

@Entity
data class Mechanic(
    val name: String
)

//    : Parcelable {
//
//    // Constructeur principal
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: ""
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        // Écrire vos champs dans le Parcel
//        parcel.writeString(name)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Mechanic> {
//        override fun createFromParcel(parcel: Parcel): Mechanic {
//            return Mechanic(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Mechanic?> {
//            return arrayOfNulls(size)
//        }
//    }
//}


@Entity
data class Cards(val Basic: List<Card> = emptyList())

