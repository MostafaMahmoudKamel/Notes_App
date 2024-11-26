package com.example.firebase_learn.domain.repository

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.data.model.UiResource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note): Flow<UiResource<Boolean>>
    suspend fun updateNote(newNote: Note):Flow<UiResource<Boolean>>
    suspend fun deleteNote(noteId: String):Flow<UiResource<Boolean>>
    suspend fun getAllNote():Flow<UiResource<List<Note>>>
    suspend fun getNoteById(noteId: String): Flow<UiResource<Note>>
    suspend fun clearNotes():Flow<UiResource<Boolean>>
//    suspend fun searchNotes(query:String):Flow<UiResource<List<Note>>>
}