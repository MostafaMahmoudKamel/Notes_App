package com.example.firebase_learn.presentition.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Register

@Serializable
data object Login

@Serializable
data object Home

@Serializable
data object Add

@Serializable
data object Splash

@Serializable
data class Update(var noteId: String)