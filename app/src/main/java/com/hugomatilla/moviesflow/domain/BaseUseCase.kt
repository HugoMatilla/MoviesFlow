package com.hugomatilla.moviesflow.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

interface BaseUseCase {
    fun execute(): Any
}

interface BaseUseCaseScoped {
    val block: suspend () -> Any
    fun execute(scope: CoroutineScope) {
        scope.launch { this@BaseUseCaseScoped.block() }
    }
}

@ExperimentalCoroutinesApi
interface BaseUseCaseStateFlow<T, R> {
    val initialValue: R
    val data: MutableStateFlow<R>
    fun transformation(it: T): R
    val block: suspend () -> StateFlow<T>

    fun execute(scope: CoroutineScope): StateFlow<R> {
        scope.launch {
            this@BaseUseCaseStateFlow.block().collect {
                data.value = transformation(it)
            }
        }
        return data
    }
}