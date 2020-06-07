package com.hugomatilla.moviesflow.domain

import kotlinx.coroutines.CoroutineScope

interface BaseUseCase {
    fun execute(): Any
}

interface BaseUseCaseSuspended {
    suspend fun execute(): Any
}

interface BaseUseCaseScoped {
    fun execute(scope: CoroutineScope): Any
}