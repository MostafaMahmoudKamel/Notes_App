package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(val noteRepository: NoteRepository) {
    suspend operator fun invoke(): Flow<UiResource<List<Note>>> {
        return noteRepository.getAllNote()
    }
}