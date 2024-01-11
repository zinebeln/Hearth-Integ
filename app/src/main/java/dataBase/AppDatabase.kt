package dataBase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.domain.repository.DaoCards
import com.example.myapplication.domain.repository.DaoUser
import com.example.myapplication.domain.repository.DecksDao
import com.example.myapplication.model.Card
import com.example.myapplication.model.DecksCard
import com.example.myapplication.model.User


@Database(entities = [Card::class, User::class, DecksCard::class],  version = 4, exportSchema = false)
@TypeConverters(CardTypeConverter::class)
//@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): DaoCards
    abstract fun userDao(): DaoUser

    abstract fun decksDao(): DecksDao

    companion object {

        private lateinit var instance: AppDatabase

        fun initDatabase(context: Context) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "hearth-stone"

       ).fallbackToDestructiveMigration()
//            .addMigrations(migration1to2, migration2to3, migration3to4)
                .build()
        }

        fun getDatabase(): AppDatabase {
            return instance ?: throw IllegalStateException("Database not initialized")
        }


        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("Migration", "Running migration from version 1 to 2")
               database.execSQL("ALTER TABLE Card ADD COLUMN img TEXT DEFAULT null")
            }
        }
        val migration2to3 = object : Migration(2, 3) {

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS users_new (username TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL)")
                database.execSQL("INSERT INTO users_new (username, password) SELECT username, password FROM users")
                database.execSQL("DROP TABLE IF EXISTS users")
                database.execSQL("ALTER TABLE users_new RENAME TO users")

        }
        }

        val migration3to4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("Migration", "Running migration from version 3 to 4")
                // Ajouter la nouvelle colonne
                database.execSQL("ALTER TABLE Card ADD COLUMN isFavorite INTEGER DEFAULT 0")
            }
        }

    }



}




