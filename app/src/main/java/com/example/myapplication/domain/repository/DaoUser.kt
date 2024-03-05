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
    @Query("SELECT userId FROM users WHERE username = :username")
    suspend fun getUserId(username: String) : Long?
    @Query("SELECT userId FROM users WHERE username = :username")
    suspend fun getUserIdd(username: String) : Long
    @Query("SELECT profileImagePath FROM users WHERE username = :username")
    suspend fun getImgPath(username: String) : String?
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
    @Query("SELECT userId FROM users WHERE username = :username")
    suspend fun getCurrentUserIdd(username: String): Long?
    @Query("UPDATE users SET profileImagePath = :imagePath WHERE userId = :userId")
    suspend fun updateUserProfileImage(userId: Long, imagePath: String)
    @Query("UPDATE users SET profileImagePath = :imagePath WHERE userId = :userId")
    suspend fun updateUserProfileImages(userId: Long, imagePath: String)
    @Delete
    suspend fun delete(user: User)
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentUser(): User?
    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE userId = :userId")
    fun updateUserLoggedInStatus(userId: Int, isLoggedIn: Boolean)



}
