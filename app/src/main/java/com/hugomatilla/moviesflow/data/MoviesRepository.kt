package com.hugomatilla.moviesflow.data

import com.hugomatilla.moviesflow.data.cloud.API_KEY
import com.hugomatilla.moviesflow.data.cloud.MovieApiService
import com.hugomatilla.moviesflow.data.cloud.SessionApiService
import com.hugomatilla.moviesflow.data.db.AppDB
import com.hugomatilla.moviesflow.data.local.LocalCache
import com.hugomatilla.moviesflow.utils.toText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.properties.Delegates.observable

class MoviesRepository : KoinComponent {
    private val appDB = AppDB.getInstance()
    fun subscribeToAllMoviesChanges() = appDB.movieDao().getAll()
    fun subscribeToNewMoviesChanges(date: Date) = appDB.movieDao().getNew(date.toText())

    suspend fun downloadNewMovies(date: Date) {
        val movies = getNewMoviesFromCloud(date.toText()).movies
        appDB.movieDao().insertAll(movies)
    }

    private val service: MovieApiService by inject()
    private suspend fun getNewMoviesFromCloud(date: String) = service.getNewMovies(API_KEY, date)
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