package com.example.firebase_learn.presentition.register

sealed class RegisterViewIntent {
    data class UpdateName(var name:String):RegisterViewIntent()
    data class UpdateEmail(var email: String) : RegisterViewIntent()
    data class UpdatePhone(var phone:String):RegisterViewIntent()
    data class UpdatePassword(var password: String) : RegisterViewIntent()
    data class Register(var name:String,var email: String,var phone: String, var password: String) : RegisterViewIntent()
}