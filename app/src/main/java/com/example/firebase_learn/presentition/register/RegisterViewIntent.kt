package com.example.firebase_learn.presentition.register

sealed class RegisterViewIntent {

    data class UpdateName(var name: String) : RegisterViewIntent()
    data class UpdateNameError(var hasError: Boolean) : RegisterViewIntent()

    data class UpdateEmail(var email: String) : RegisterViewIntent()
    data class UpdateEmailError(var hasError: Boolean) : RegisterViewIntent()

    data class UpdatePassword(var password: String) : RegisterViewIntent()
    data class UpdatePasswordError(var hasError: Boolean):RegisterViewIntent()
    data object UpdatePasswordVisisbility : RegisterViewIntent()


    data class UpdateConfirmPassword(var confirmPassword : String) : RegisterViewIntent()
    data class UpdateConfirmPasswordError(var hasError: Boolean):RegisterViewIntent()
    data object UpdateConfirmPasswordVisisbality : RegisterViewIntent()


    data class Register(
        var name: String,
        var email: String,
        var phone: String,
        var password: String
    ) : RegisterViewIntent()

}