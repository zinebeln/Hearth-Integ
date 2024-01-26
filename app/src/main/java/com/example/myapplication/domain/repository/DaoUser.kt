package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
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

    @Insert
    suspend fun insertt(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)


    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsernameT(username: String): User

//    @Query("SELECT * FROM users WHERE userId = :id")
//    suspend fun getCurrentUserId(id: Long): Long?

    @Query("SELECT userId FROM users WHERE username = :username")
    suspend fun getCurrentUserIdd(username: String): Long?

    //
    //    @Query("SELECT userId FROM users )
//    suspend fun getUserId(): User


    @Delete
    suspend fun delete(user: User)


    //---changement ici
//    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE username = :username")
//    suspend fun updateUserLoginStatus(username: String?, isLoggedIn: Boolean)
//
//    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
//    suspend fun getLoggedInUser(): User?
//
//    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
//    fun getLoggedInUserLiveData(): LiveData<User>
//
//    @Query("SELECT id FROM users WHERE isLoggedIn = 1 LIMIT 1")
//    suspend fun getLoggedInUserId(): String?

}
