package com.hugomatilla.moviesflow.di

import com.hugomatilla.moviesflow.data.MoviesRepository
import com.hugomatilla.moviesflow.data.SessionRepository
import com.hugomatilla.moviesflow.data.cloud.CloudService
import com.hugomatilla.moviesflow.data.local.LocalCache
import com.hugomatilla.moviesflow.home.domain.SubscribeToMoviesChanges
import com.hugomatilla.moviesflow.home.domain.SubscribeToRandomMoviesUseCaSe
import com.hugomatilla.moviesflow.home.domain.UpdateRandomMovieUseCaSe
import com.hugomatilla.moviesflow.home.presentation.MoviesViewModel
import com.hugomatilla.moviesflow.random_movie.RandomMovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocalCache(get()) }
    single { MoviesRepository() }
    single { SessionRepository() }
    single { CloudService().createMovieApis() }
    single { CloudService().createSessionApi() }

    factory { SubscribeToMoviesChanges() }
    single { SubscribeToRandomMoviesUseCaSe() }
    single { UpdateRandomMovieUseCaSe() }

    viewModel { MoviesViewModel() }
    viewModel { RandomMovieViewModel() }
}