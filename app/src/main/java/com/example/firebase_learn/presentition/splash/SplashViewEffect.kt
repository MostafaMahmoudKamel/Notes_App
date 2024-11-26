package com.example.firebase_learn.presentition.splash

sealed class SplashViewEffect {
    data object NavigateToLogin :SplashViewEffect()
    data object NavigateToHome:SplashViewEffect()
}