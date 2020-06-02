package com.hugomatilla.moviesflow.random_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hugomatilla.moviesflow.home.domain.SubscribeToRandomMoviesUseCaSe
import com.hugomatilla.moviesflow.home.domain.UpdateRandomMovieUseCaSe
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class RandomMovieViewModel : ViewModel(), KoinComponent {

    private val subscription: SubscribeToRandomMoviesUseCaSe by inject()
    private val updateMovie: UpdateRandomMovieUseCaSe by inject()
    lateinit var data: LiveData<String>

    init {
        viewModelScope.launch() {
            data = subscription.execute().asLiveData()
        }
    }

    fun getNewMovie() {
        viewModelScope.launch { updateMovie.execute() }
    }

}