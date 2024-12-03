package com.example.firebase_learn.utils

sealed class UiResource<out T>() {
    data object Loading : UiResource<Nothing>()
    data class Success<T>(var data: T) : UiResource<T>()
    data class Failure(var exception: Exception) : UiResource<Nothing>()
}