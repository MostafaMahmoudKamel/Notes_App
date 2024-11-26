package com.example.firebase_learn.presentition.update

import com.example.firebase_learn.data.model.Note

sealed class UpdateViewIntent {
    data class SetTitle(var title:String):UpdateViewIntent()
    data class SetData(var data:String):UpdateViewIntent()
    data class UpdateNote(var note: Note):UpdateViewIntent()
    data class DeleteNote(var noteId:String):UpdateViewIntent()
}