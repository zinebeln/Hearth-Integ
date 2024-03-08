package dataBase

import androidx.room.TypeConverter
import com.example.myapplication.model.Card
import com.google.gson.Gson

class CardTypeConverter {
    @TypeConverter
    fun fromCard(card: Card): String {
        // Convertir la carte en une représentation json
        return Gson().toJson(card)
    }

    @TypeConverter
    fun toCard(cardString: String): Card {
        // Convertir la représentation de chaîne en objet Card
        return Gson().fromJson(cardString, Card::class.java)
    }
}