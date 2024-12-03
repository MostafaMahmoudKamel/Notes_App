package com.example.firebase_learn.domain.usecase.noteUseCase

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.utils.UiResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchNoteUseCase @Inject constructor(private val getAllNotesUseCase: GetAllNotesUseCase) {
    suspend operator fun invoke(searchQuery: String): Flow<UiResource<List<Note>>> {
        return getAllNotesUseCase().map { uiResourse ->
            when (uiResourse) {
                is UiResource.Loading -> UiResource.Loading
                is UiResource.Success -> {
                    val filteredNotes = uiResourse.data.filter { note ->
                        note.title.startsWith(searchQuery, ignoreCase = true) ||
                                note.data.startsWith(searchQuery, ignoreCase = true)
                    }
                    UiResource.Success(filteredNotes)//without emit new item . it modify founded item
                }

                is UiResource.Failure -> UiResource.Failure(uiResourse.exception)
            }


        }
    }


}



