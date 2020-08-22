package com.hugomatilla.moviesflow.home.domain

import com.hugomatilla.moviesflow.data.SessionRepository
import com.hugomatilla.moviesflow.domain.BaseUseCaseScoped
import com.hugomatilla.moviesflow.domain.BaseUseCaseStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class SubscribeToRandomMoviesUseCaSe() : BaseUseCaseStateFlow<String, String>, KoinComponent {

    private val repo: SessionRepository by inject()

    override val initialValue: String = "empty"
    override val data = MutableStateFlow(initialValue)
    override fun transformation(it: String): String = if (it.isEmpty()) initialValue else it
    override val block: suspend () -> StateFlow<String> = { repo.subscribeToRandomMovieUpdates() }

}

@ExperimentalCoroutinesApi
class UpdateRandomMovieUseCaSe() : BaseUseCaseScoped, KoinComponent {
    private val repo: SessionRepository by inject()
    override val block: suspend () -> Any = { repo.updateRandomMovie() }
}