package com.example.firebase_learn.presentition.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.domain.usecase.noteUseCase.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<AddUiState> = MutableStateFlow(AddUiState())
    var uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    private var _effectFlow = MutableSharedFlow<AddViewEffect>()
    var effectFlow: SharedFlow<AddViewEffect> = _effectFlow.asSharedFlow()

    fun handeIntent(intent: AddViewIntent) {
        when (intent) {

            is AddViewIntent.AddNote -> {
                addNote(note = intent.note)
            }

            is AddViewIntent.SetTitle -> {
                setTitle(intent.title)
            }

            is AddViewIntent.SetData -> {
                setData(intent.data)
            }
        }


    }

    private fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    private fun setData(data: String) {
        _uiState.value = _uiState.value.copy(data = data)
    }

    private fun addNote(note: Note) {
        Log.i("AddViewModel", "Adding ViewModelScope before launch${Thread.currentThread().name}")
        viewModelScope.launch(Dispatchers.Default) {
            Log.i("AddViewModel", "Adding ViewModelScope inside launch ${Thread.currentThread().name}")
            addNoteUseCase(note).collect { uiResoures ->
                when (uiResoures) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true) //allNOtes
                    }

                    is UiResource.Success -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = false, title = "", data = "")
                        _effectFlow.emit(AddViewEffect.NavigateToHome)   //emit effect
                    }

                    is UiResource.Failure -> {
                        _uiState.value = _uiState.value.copy(isLoading = false) //allNOtes

                    }
                }
            }
        }
        Log.i("AddViewModel", "Adding ViewModelScope after launch ${Thread.currentThread().name}")
    }

}