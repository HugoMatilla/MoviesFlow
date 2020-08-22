package com.hugomatilla.moviesflow.random_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hugomatilla.moviesflow.home.domain.SubscribeToRandomMoviesUseCaSe
import com.hugomatilla.moviesflow.home.domain.UpdateRandomMovieUseCaSe
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.inject

class RandomMovieViewModel : ViewModel(), KoinComponent {

    private val subscription: SubscribeToRandomMoviesUseCaSe by inject()
    private val updateMovie: UpdateRandomMovieUseCaSe by inject()

    @InternalCoroutinesApi
    val data: LiveData<String> = subscription.execute(viewModelScope).asLiveData()
//
//    init {
//        viewModelScope.launch() {
//            data = subscription.execute().asLiveData()
//        }
//    }

    fun getNewMovie() {
        updateMovie.execute(viewModelScope)
    }
}