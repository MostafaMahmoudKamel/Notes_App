package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.domain.repository.NoteRepository
import com.example.firebase_learn.utils.UiResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClearNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(): Flow<UiResource<Boolean>> {
        return noteRepository.clearNotes()
    }
}