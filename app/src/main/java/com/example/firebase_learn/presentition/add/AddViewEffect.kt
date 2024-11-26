package com.example.firebase_learn.presentition.add

sealed class AddViewEffect {
    class ShowSnackBar(var message: String) : AddViewEffect()
    data object NavigateToHome : AddViewEffect()
}