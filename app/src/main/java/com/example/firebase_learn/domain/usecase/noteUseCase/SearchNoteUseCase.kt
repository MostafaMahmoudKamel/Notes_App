package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.data.model.UiResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchNoteUseCase @Inject constructor(var getAllNotesUseCase: GetAllNotesUseCase) {
    suspend operator fun invoke(searchQuery: String): Flow<UiResource<List<Note>>> = flow {
        getAllNotesUseCase().collect{ uiResourse ->
            when (uiResourse) {
                is UiResource.Loading -> {
                   emit( UiResource.Loading)
                }

                is UiResource.Success -> {
                    val filteredNotes = uiResourse.data.filter { note ->
                        note.title.startsWith(searchQuery, ignoreCase = true) || note.data.startsWith(searchQuery,ignoreCase = true)
                    }
                    emit(UiResource.Success(filteredNotes))
                }

                is UiResource.Failure -> {
                  emit(UiResource.Failure(exception = uiResourse.exception))
                }
            }
        }

    }
}