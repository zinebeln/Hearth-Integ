package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.User

@Dao
interface DaoUser {

    @Update
    suspend fun updateUser(user: User)
     @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE userId = :userId")
     suspend fun updateUserLoggedInStatus(userId: Long, isLoggedIn: Boolean)
    @Insert
    suspend fun insert(user: User)

    @Insert
    suspend fun insertt(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsernameT(username: String): User

   @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
   suspend fun getUserByUsername(username: String): User?

    @Query("SELECT userId FROM users WHERE username = :username")
    suspend fun getCurrentUserIdd(username: String): Long?

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentUser(): User?



}
