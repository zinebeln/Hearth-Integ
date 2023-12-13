package dataBase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.HearthStone
import com.example.myapplication.domain.CardDataService
import com.example.myapplication.domain.repository.DaoCards
import com.example.myapplication.model.Card


@Database(entities = [Card::class],  version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): DaoCards

    companion object {

        private lateinit var instance: AppDatabase

        fun initDatabase(context: Context) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "hearth-stone"

       ).addMigrations(migration1to2)
                .build()
        }

        fun getDatabase(): AppDatabase {
            return instance ?: throw IllegalStateException("Database not initialized")
        }

        fun getInstance(): AppDatabase {
            return instance
        }

        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("Migration", "Running migration from version 1 to 2")
               database.execSQL("ALTER TABLE Card ADD COLUMN img TEXT DEFAULT null")
            }
        }
    }



}




