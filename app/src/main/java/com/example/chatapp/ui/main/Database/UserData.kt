package com.example.chatapp.ui.main.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDb (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id:Int,

    @ColumnInfo(name="username")
    val userName:String?,

    @ColumnInfo(name="email")
    val userEmail:String?,

    @ColumnInfo(name="logState")
    val logState:Boolean
)