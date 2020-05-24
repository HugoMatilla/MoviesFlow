package com.hugomatilla.moviesflow

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        (Movie::class)
    ],
    version = 6
)
abstract class DB : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        @Volatile
        private var INSTANCE: DB? = null

        private const val dbName = "movies.db"

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(DB::class) {
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext, DB::class.java, dbName)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
        }

        fun getInstance(): DB {
            if (INSTANCE == null) {
                Log.wtf("Room", "Room Database not initialized")
                throw(IllegalAccessError())
            } else
                return INSTANCE!!

        }

        private fun destroyInstance() {
            INSTANCE = null
        }
    }
}