package com.example.chatapp.ui.main.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao{

    @Query("Select *from user")
    fun getUser():UserDb

    @Insert
    fun addUser(vararg user:UserDb)

    @Delete
    fun logOutUser(who : UserDb)
}