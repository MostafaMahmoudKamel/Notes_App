package com.example.firebase_learn.presentition.register

sealed class RegisterViewEffect {
    class ShowSnackBar(var message: String): RegisterViewEffect()
    data object NavigateToLogin: RegisterViewEffect()

}