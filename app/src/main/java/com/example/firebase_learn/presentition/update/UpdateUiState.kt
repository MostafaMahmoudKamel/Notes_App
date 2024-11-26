package com.example.firebase_learn.presentition.update

data class UpdateUiState(
    var isLoading:Boolean=false,
    var title: String = "",
    var data: String = "",
    var noteId:String="",
    var userId:String=""

    )