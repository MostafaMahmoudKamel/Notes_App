package com.example.firebase_learn.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> wrapWithFlow(function: suspend () -> T): Flow<UiResource<T>> = flow {
    emit(UiResource.Loading)
    delay(500)
    try {
        val result = function()
        emit(UiResource.Success(result))
    } catch (e: Exception) {
        emit(UiResource.Failure(e))
    }
}