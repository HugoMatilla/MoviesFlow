package com.hugomatilla.moviesflow.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DB_NAME = "movies.db"

@Database(entities = [(Movie::class)], version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        @Volatile
        private var instance: AppDB? = null

        fun init(context: Context) {
            if (instance == null) synchronized(AppDB::class) { instance = buildDB(context) }
        }

        private fun buildDB(context: Context) =
            Room.databaseBuilder(context, AppDB::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance() = instance
            ?: run {
                Log.wtf("Room", "Room Database not initialized. Call `AppDB.init(context)` first")
                throw(IllegalAccessError())
            }

        private fun destroyInstance() {
            instance = null
        }
    }
}