package com.hugomatilla.moviesflow.data

import com.hugomatilla.moviesflow.data.db.AppDB

class MoviesRepository {
    fun getDB() = AppDB.getInstance()
}