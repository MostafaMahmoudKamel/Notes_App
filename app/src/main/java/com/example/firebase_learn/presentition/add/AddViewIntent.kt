package com.example.firebase_learn.presentition.add

import com.example.firebase_learn.data.model.Note

sealed class AddViewIntent(){
    data class SetTitle(var title:String): AddViewIntent()
    data class SetData(var data:String): AddViewIntent()
    data class AddNote(var note: Note): AddViewIntent()
}