package com.example.myapplication.model


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "users", indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val username: String,
    var password: String,
    val isLoggedIn: Boolean = false,
    val profileImagePath: String?

)
