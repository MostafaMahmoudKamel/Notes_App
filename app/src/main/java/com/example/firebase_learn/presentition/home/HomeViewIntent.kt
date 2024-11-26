package com.example.firebase_learn.presentition.home

sealed class HomeViewIntent {
    data object Logout : HomeViewIntent()
    data object NavigateToAdd : HomeViewIntent()
    data class SetSearchTxt(var search: String) : HomeViewIntent()
    data object SearchQuery : HomeViewIntent()
    data object ClearSearchTxt : HomeViewIntent()

}















































