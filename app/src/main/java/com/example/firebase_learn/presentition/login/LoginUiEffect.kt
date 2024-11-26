package com.example.firebase_learn.presentition.login

sealed class LoginUiEffect {
    class ShowSnackBar(var message: String): LoginUiEffect()
    data object NavigateToHome: LoginUiEffect()
    data object NavigateToRegister: LoginUiEffect()
}