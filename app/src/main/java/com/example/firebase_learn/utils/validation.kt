package com.example.firebase_learn.utils

fun String.isEmailValid(): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    return this.matches(emailRegex)
}

fun String.isNameValid():Boolean{
    return this.length>=5
}

fun String.isPasswordValid():Boolean{
    return this.length>=6
}

fun String.isPasswordMatches(password:String):Boolean{
    return this==password
}

