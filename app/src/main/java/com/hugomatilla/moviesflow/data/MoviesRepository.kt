package com.hugomatilla.moviesflow.data

import com.hugomatilla.moviesflow.data.cloud.API_KEY
import com.hugomatilla.moviesflow.data.cloud.SessionApiService
import com.hugomatilla.moviesflow.data.db.AppDB
import com.hugomatilla.moviesflow.data.local.LocalCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.properties.Delegates.observable

class MoviesRepository {
    fun getDB() = AppDB.getInstance()
}

@ExperimentalCoroutinesApi
class SessionRepository : KoinComponent {
    private val cache: LocalCache by inject()
    private val service: SessionApiService by inject()
    private var currentYear = "2010"
    private val data = MutableStateFlow("")

    suspend fun subscribeToRandomMovieUpdates(): StateFlow<String> {
        data.value = getCurrentRandomMovie()
        return data
    }

    private suspend fun getCurrentRandomMovie(): String {
        if (cache.randomMovie.isEmpty()) cache.randomMovie = getRandomMovie()
        return cache.randomMovie
    }

    private var randomMovie: String by observable("untitled") { _, _, newValue ->
        cache.randomMovie = newValue
        data.value = "$currentYear: $newValue"
    }

    suspend fun updateRandomMovie() {
        randomMovie = getRandomMovie()
    }

    private suspend fun getRandomMovie() = service.getRandomMovie(API_KEY, year).movies[0].title

    private val year: String get() = (1980..2020).random().toString().also { currentYear = it }

}