package com.example.firebase_learn.presentition.home

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.data.model.User

data class HomeUiState(
    var isLoading:Boolean=false,
    var lNotes:List<Note> = emptyList(),
    var search:String=""
)