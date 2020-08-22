package com.hugomatilla.moviesflow.home.domain

import com.hugomatilla.moviesflow.data.MoviesRepository
import com.hugomatilla.moviesflow.data.db.Movie
import com.hugomatilla.moviesflow.domain.BaseUseCase
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject

class SubscribeToFavMoviesChanges() : BaseUseCase, KoinComponent {

    private val repo: MoviesRepository by inject()

    override fun execute(): Flow<List<Movie>> {
        return repo.subscribeToFavMoviesChanges()
    }

}
