package com.example.chatapp.ui.main.Database
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDb::class], version=1)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao():UserDao
/*
    companion object{
        @Volatile private var instance:UserDatabase?=null
        private val LOCK=Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance=it }
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(context,UserDatabase::class.java,"user.db").build()
    }

 */
}
