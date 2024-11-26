package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(noteId:String): Flow<UiResource<Boolean>> {
        return noteRepository.deleteNote(noteId = noteId)
    }
}