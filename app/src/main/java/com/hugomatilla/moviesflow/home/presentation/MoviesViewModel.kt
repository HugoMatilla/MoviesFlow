package com.hugomatilla.moviesflow.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hugomatilla.moviesflow.home.domain.GetNewMoviesUseCase
import com.hugomatilla.moviesflow.home.domain.SubscribeToFavMoviesChanges
import com.hugomatilla.moviesflow.home.domain.SubscribeToMoviesChanges
import com.hugomatilla.moviesflow.home.domain.SubscribeToNewMoviesChangesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesViewModel : ViewModel(), KoinComponent {

    private val subscribeToMoviesChanges: SubscribeToMoviesChanges by inject()
    private val subscribeToFavMoviesChanges: SubscribeToFavMoviesChanges by inject()
    private val subscribeToNewMoviesChanges: SubscribeToNewMoviesChangesUseCase by inject()
    private val getNewMoviesUseCase: GetNewMoviesUseCase by inject()

    val favMovies = subscribeToFavMoviesChanges
        .execute()
        .conflate()
        .asLiveData()

    val allMovies = subscribeToMoviesChanges
        .execute()
        .conflate()
        .asLiveData()

    val newMovies = subscribeToNewMoviesChanges
        .execute()
        .conflate()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO) // TODO Needed?

    fun getNewMovies() {
        getNewMoviesUseCase.execute(viewModelScope)
    }
}