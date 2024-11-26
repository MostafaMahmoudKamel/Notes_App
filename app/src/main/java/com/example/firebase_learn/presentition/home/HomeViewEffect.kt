package com.example.firebase_learn.presentition.home

sealed class HomeViewEffect {
    class ShowSnackBar(var message: String) : HomeViewEffect()
    data object NavigateToLogin : HomeViewEffect()
    data object NavigateToAdd:HomeViewEffect()
}