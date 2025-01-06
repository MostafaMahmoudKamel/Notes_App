package com.example.firebase_learn.presentition.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.domain.usecase.dataStoreUsecase.UpdateBooleanUseCase
import com.example.firebase_learn.domain.usecase.noteUseCase.ClearNotesUseCase
import com.example.firebase_learn.domain.usecase.noteUseCase.GetAllNotesUseCase
import com.example.firebase_learn.domain.usecase.noteUseCase.SearchNoteUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.GetFireStoreDataUserUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.LogoutUserUseCase
import com.example.firebase_learn.utils.UiResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var logoutUserUseCase: LogoutUserUseCase,
    private var fireStoreDataUserUseCase: GetFireStoreDataUserUseCase,
    private var getAllNotesUseCase: GetAllNotesUseCase,
    private var searchNoteUseCase: SearchNoteUseCase,
    private var clearNotesUseCase: ClearNotesUseCase,
    private var updateBooleanUseCase: UpdateBooleanUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var _effectFlow = MutableSharedFlow<HomeViewEffect>()
    var effectFlow: SharedFlow<HomeViewEffect> = _effectFlow.asSharedFlow()


    fun handeIntent(intent: HomeViewIntent) {
        when (intent) {
            is HomeViewIntent.Logout -> logout()
            is HomeViewIntent.NavigateToAdd -> navigateToAdd()
            is HomeViewIntent.SetSearchTxt -> setSearchTxt(intent.search)
            is HomeViewIntent.ClearSearchTxt -> clearSearchTxt()
            is HomeViewIntent.SearchQuery -> searchQuery()
            is HomeViewIntent.GetAllNotes -> getAllNOtes()
            is HomeViewIntent.ClearAllNotes -> clearAllNotes()
            is HomeViewIntent.SetExpand -> setExpand(intent.expand)
        }


    }

    private fun setSearchTxt(search: String) {
        _uiState.update { it.copy(search = search) }
    }

    private fun clearSearchTxt() {
        _uiState.update { it.copy(search = "") }
    }

    private fun setExpand(expand: Boolean) {
        _uiState.update { it.copy(expandSetting = expand) }
    }

    private fun navigateToAdd() {
        viewModelScope.launch {
            _effectFlow.emit(HomeViewEffect.NavigateToAdd)
        }
    }

    private fun clearAllNotes() {
        viewModelScope.launch {
            clearNotesUseCase().collect { uiResource ->

                when (uiResource) {
                    is UiResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is UiResource.Success -> {
                        _uiState.update { it.copy(isLoading = false, lNotes = emptyList()) }
                    }

                    is UiResource.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }


            }
        }
    }

    private fun searchQuery() {
        viewModelScope.launch {
            searchNoteUseCase(uiState.value.search).collect { uiResourse ->
                when (uiResourse) {
                    is UiResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is UiResource.Success -> {
                        _uiState.update { it.copy(lNotes = uiResourse.data, isLoading = false) }
                    }

                    is UiResource.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }

            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUserUseCase().collect { uiResoure ->
                when (uiResoure) {
                    is UiResource.Loading -> { //changing loading ui so i should convert to Dispatchers.main
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is UiResource.Success -> {
                        _effectFlow.emit(HomeViewEffect.ShowSnackBar(message = "Success logout"))
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _effectFlow.emit(HomeViewEffect.NavigateToLogin)

                        //clear shared preferences
//                            clearPreferenceUseCase()
                        updateBooleanUseCase(value = false)//update to defaultValues     //change it

                    }

                    is UiResource.Failure -> {
                        _effectFlow.emit(HomeViewEffect.ShowSnackBar(message = "failLogout"))
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }


            }
        }
    }

    //get user data information
    private fun getUserData() {
        viewModelScope.launch {
            fireStoreDataUserUseCase().collect { uiResourse ->
                when (uiResourse) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is UiResource.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }

                    is UiResource.Failure -> {
                        _effectFlow.emit(HomeViewEffect.ShowSnackBar(message = "fail getData"))
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }

            }
        }
    }


    private fun getAllNOtes() {
        viewModelScope.launch {
            getAllNotesUseCase().collect { uiResoures ->
                when (uiResoures) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true) //allNOtes
                    }

                    is UiResource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            lNotes = uiResoures.data,
                            isLoading = false
                        )

                    }

                    is UiResource.Failure -> {
                        _uiState.value = _uiState.value.copy(isLoading = false) //allNOtes
                    }
                }
            }
        }
    }


    init {
        getUserData()
        getAllNOtes()
//        searchQuery()  //put in valueChange of editText

    }
}

