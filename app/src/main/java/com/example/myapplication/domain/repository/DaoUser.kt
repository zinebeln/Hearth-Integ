package com.example.myapplication.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.User

@Dao
interface DaoUser {
    @Insert
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)


    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsernameT(username: String): User

    @Delete
    suspend fun delete(user: User)

}
