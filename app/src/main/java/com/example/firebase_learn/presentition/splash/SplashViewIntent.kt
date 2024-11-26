package com.example.firebase_learn.presentition.splash

sealed class SplashViewIntent {
    data object CheckUserStatus:SplashViewIntent()
}