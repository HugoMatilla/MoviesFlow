package com.hugomatilla.moviesflow.di

import com.hugomatilla.moviesflow.data.MoviesRepository
import com.hugomatilla.moviesflow.home.domain.SubscribeToMoviesChanges
import com.hugomatilla.moviesflow.home.presentation.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { MoviesRepository() }
    factory { SubscribeToMoviesChanges() }
    viewModel { MoviesViewModel() }
}