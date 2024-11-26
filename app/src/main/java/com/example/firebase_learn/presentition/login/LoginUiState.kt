package com.example.firebase_learn.presentition.login

import com.example.firebase_learn.data.model.User

data class LoginUiState(
    var isLoading:Boolean=false,
    var email:String="",
    var password:String="",
    var errorMessage:String="",
    var userInfo: User = User(name="")
)
