package com.hugomatilla.moviesflow.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface BaseUseCase {
    fun execute(dispatcher: CoroutineDispatcher = Dispatchers.IO): Any
}