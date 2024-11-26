package com.example.firebase_learn.presentition.register

import com.example.firebase_learn.data.model.User

data class RegisterUiState(
    var isLoading: Boolean = false,
    var name:String="",
    var email: String = "",
    var phoneNumber:String="",
    var password: String = "",
    var errorMessage: String = "",
    var userInfo: User = User(name = "")
)

