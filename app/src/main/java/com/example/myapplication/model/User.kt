package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "users", indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,  // Ajout du champ auto-incrémenté
//    @PrimaryKey val username: String,
    val username: String,
//    @ColumnInfo(name = "Id") val id: String = UUID.randomUUID().toString(),
    val password: String,
//    val isLoggedIn: Boolean = false
)
