package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note): Flow<UiResource<Boolean>> {
       return noteRepository.addNote(note)
    }
}