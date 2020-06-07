package com.hugomatilla.moviesflow.home.domain

import com.hugomatilla.moviesflow.data.MoviesRepository
import com.hugomatilla.moviesflow.data.db.Movie
import com.hugomatilla.moviesflow.domain.BaseUseCase
import com.hugomatilla.moviesflow.domain.BaseUseCaseScoped
import com.hugomatilla.moviesflow.utils.oneWeekAgo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SubscribeToNewMoviesChangesUseCase() : BaseUseCase, KoinComponent {

    private val repo: MoviesRepository by inject()

    override fun execute(): Flow<List<Movie>> {
        return repo.subscribeToNewMoviesChanges(oneWeekAgo)
    }
}


class GetNewMoviesUseCase() : BaseUseCaseScoped, KoinComponent {

    private val repo: MoviesRepository by inject()

    override fun execute(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            repo.downloadNewMovies(oneWeekAgo)
        }
    }

}

