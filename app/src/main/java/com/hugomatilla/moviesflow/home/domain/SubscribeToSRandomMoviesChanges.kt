package com.hugomatilla.moviesflow.home.domain

import com.hugomatilla.moviesflow.data.SessionRepository
import com.hugomatilla.moviesflow.domain.BaseUseCaseSuspended
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject


//fun Movie.toRandomMovie() = RandomMovie(title, overview, image)
//
//data class RandomMovie(val title: String, val overview: String, val imageUrl: String)

class SubscribeToRandomMoviesUseCaSe() : BaseUseCaseSuspended, KoinComponent {

    private val repo: SessionRepository by inject()
    val data = MutableStateFlow("empty")

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun execute(): StateFlow<String> {
        GlobalScope.launch(Dispatchers.IO) {
            repo.subscribeToRandomMovieUpdates().collect {
                data.value = if (it.isEmpty()) "empty" else it
            }
        }
        return data
    }
}

class UpdateRandomMovieUseCaSe() : BaseUseCaseSuspended, KoinComponent {
    private val repo: SessionRepository by inject()
    override suspend fun execute() {
        repo.updateRandomMovie()
    }
}