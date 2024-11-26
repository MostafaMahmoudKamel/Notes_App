package com.example.firebase_learn.presentition.add

data class AddUiState(
    var isLoading: Boolean = false,
    var title: String = "",
    var data: String = "",
)