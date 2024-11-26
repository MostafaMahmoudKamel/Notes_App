package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.domain.repository.NoteRepository
import javax.inject.Inject

class ClearNotesUseCase @Inject constructor(var noteRepository: NoteRepository) {
    suspend operator fun invoke(){
        noteRepository.clearNotes()
    }
}