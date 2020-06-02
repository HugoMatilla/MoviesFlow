package com.hugomatilla.moviesflow.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hugomatilla.moviesflow.data.db.Movie
import com.hugomatilla.moviesflow.home.domain.SubscribeToMoviesChanges
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesViewModel : ViewModel(), KoinComponent {

    private val subscribeToMoviesChanges: SubscribeToMoviesChanges by inject()

    lateinit var data: LiveData<List<Movie>>
        private set

    init {
        viewModelScope.launch() {
            data = subscribeToMoviesChanges.execute().conflate().asLiveData()
        }
    }
}