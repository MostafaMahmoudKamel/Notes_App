package com.example.firebase_learn.presentition.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.domain.repository.UserRepository
import com.example.firebase_learn.domain.usecase.userUseCase.GetFireStoreDataUserUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.LogoutUserUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.RegisterUserUseCase
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
class RegisterViewModel @Inject constructor(
//    private var userRepository: UserRepository,
    private var registerUserUseCase: RegisterUserUseCase,

    ) : ViewModel() {

    private var _uiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState())
    var uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()


    private var _effectFlow = MutableSharedFlow<RegisterViewEffect>()
    var effectFlow: SharedFlow<RegisterViewEffect> = _effectFlow.asSharedFlow()


    fun handeIntent(intent: RegisterViewIntent) {
        when (intent) {
            is RegisterViewIntent.UpdateName->{updateName(intent.name)}
            is RegisterViewIntent.UpdateEmail -> { updateEmail(intent.email) }
            is RegisterViewIntent.UpdatePhone->{updatePhone(intent.phone)}
            is RegisterViewIntent.UpdatePassword -> { updatePassword(intent.password) }
            is RegisterViewIntent.Register -> { register(intent.name,intent.email,intent.phone, intent.password) }

        }

    }

    private fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    private fun updateEmail(inputEmail: String) {
        _uiState.value = _uiState.value.copy(email = inputEmail)
    }
    private fun updatePhone(phone:String){
        _uiState.value=_uiState.value.copy(phoneNumber = phone)
    }

    private fun updatePassword(inputPassword: String) {
        _uiState.value = _uiState.value.copy(password = inputPassword)
    }

    private fun register(name:String,email: String,phone: String ,password: String) {

        viewModelScope.launch {

            _effectFlow.emit(RegisterViewEffect.ShowSnackBar("Register Screen")) ///launched effect
            registerUserUseCase(name =name, email, phone = phone, password = password).collect { uiResourse ->
                when (uiResourse) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is UiResource.Success -> {
                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                email = "",
                                password = ""
                            )//clear textFeild
                        _effectFlow.emit(RegisterViewEffect.NavigateToLogin)//navigate to home //navigate effect

                    }

                    is UiResource.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = uiResourse.exception.message ?: "UnKnownError"
                        )
                    }
                }

            }
        }

    }

}
