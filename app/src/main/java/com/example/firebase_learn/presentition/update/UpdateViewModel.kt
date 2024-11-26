package com.example.firebase_learn.presentition.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.domain.usecase.noteUseCase.DeleteNoteUseCase
import com.example.firebase_learn.domain.usecase.noteUseCase.GetNoteByIdUseCase
import com.example.firebase_learn.domain.usecase.noteUseCase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
//    private var noteRepository: NoteRepository
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(UpdateUiState())
    var uiState = _uiState.asStateFlow()

    private var _effectFlow = MutableSharedFlow<UpdateViewEffect>()
    var effectFlow = _effectFlow.asSharedFlow()

    fun handleIntent(intent: UpdateViewIntent) {
        when (intent) {
            is UpdateViewIntent.UpdateNote -> {
                updateNote(intent.note)
            }

            is UpdateViewIntent.DeleteNote -> {
                deleteNote(intent.noteId)
            }

            is UpdateViewIntent.SetTitle -> setTitle(intent.title)
            is UpdateViewIntent.SetData -> setData(intent.data)
        }
    }

    private fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    private fun setData(data: String) {
        _uiState.value = _uiState.value.copy(data = data)
    }


    fun getDataOfNote(noteId: String) {
        viewModelScope.launch {

            getNoteByIdUseCase(noteId=noteId).collect { uiResourse ->
                when (uiResourse) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is UiResource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            title = uiResourse.data.title,
                            data = uiResourse.data.data,
                            userId = uiResourse.data.userId,
                            noteId = uiResourse.data.noteId
                        )
                    }

                    is UiResource.Failure -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)

                    }
                }

            }
        }
        _uiState.value = _uiState.value.copy()
    }

    private fun updateNote(newNote: Note) {
        viewModelScope.launch {

            updateNoteUseCase(newNote = newNote).collect { uiResoures ->
                when (uiResoures) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is UiResource.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _effectFlow.emit(UpdateViewEffect.NavigateToHome)

                    }

                    is UiResource.Failure -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }

            }

        }

    }

    private fun deleteNote(noteId: String) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId = noteId).collect { uiResourse ->
                when (uiResourse) {
                    is UiResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is UiResource.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effectFlow.emit(UpdateViewEffect.NavigateToHome) //effect

                    }

                    is UiResource.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }

            }
        }
    }
}