package dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.HearthStone
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.domain.repository.DaoCards
import com.example.myapplication.model.Card

@Database(entities = [Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): DaoCards

    companion object {

        private lateinit var instance: AppDatabase

        fun initDatabase(context: Context) {
            instance = Room.databaseBuilder(
                context, AppDatabase::class.java,
                "hearth-stone"
            )
                .build()
        }

        fun getInstance(): AppDatabase {
            return instance
        }
    }
}


