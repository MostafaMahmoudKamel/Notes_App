package com.example.firebase_learn.presentition.login

sealed class LoginViewIntent {
    data class UpdateEmail(var email: String) : LoginViewIntent()
    data class UpdatePassword(var password: String) : LoginViewIntent()
    data class Login(var email: String, var password: String) : LoginViewIntent()//signIn
    data object HandleNavigateToRegister : LoginViewIntent()
}