package com.example.firebase_learn.presentition.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.domain.usecase.userUseCase.RegisterUserUseCase
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
class RegisterViewModel @Inject constructor(
    private var registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {

    private var _uiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState())
    var uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()


    private var _effectFlow = MutableSharedFlow<RegisterViewEffect>()
    var effectFlow: SharedFlow<RegisterViewEffect> = _effectFlow.asSharedFlow()


    fun handeIntent(intent: RegisterViewIntent) {
        when (intent) {
            is RegisterViewIntent.UpdateName -> {
                updateName(intent.name)
            }

            is RegisterViewIntent.UpdateNameError -> {
                updateNameError(intent.hasError)
            }

            is RegisterViewIntent.UpdateEmail -> {
                updateEmail(intent.email)
            }

            is RegisterViewIntent.UpdateEmailError -> {
                updateEmailError(hasError = intent.hasError)
            }

            is RegisterViewIntent.UpdatePassword -> {
                updatePassword(intent.password)
            }

            is RegisterViewIntent.UpdatePasswordError -> {
                updatePasswordError(intent.hasError)
            }

            is RegisterViewIntent.UpdatePasswordVisisbility -> {
                updatePasswordVisibility()
            }

            is RegisterViewIntent.UpdateConfirmPassword -> {
                updateConfirmPassword(intent.confirmPassword)
            }

            is RegisterViewIntent.UpdateConfirmPasswordError -> {
                updateConfirmPasswordError(intent.hasError)
            }

            is RegisterViewIntent.UpdateConfirmPasswordVisisbality -> {
                updateConfirmPasswordVisibility()
            }

            is RegisterViewIntent.Register -> {
                register(intent.name, intent.email, intent.phone, intent.password)
            }


        }

    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    private fun updateNameError(hasError: Boolean) {
        _uiState.update { it.copy(nameError = hasError) }
    }


    private fun updateEmail(inputEmail: String) {
        _uiState.update { it.copy(email = inputEmail) }
    }

    private fun updateEmailError(hasError: Boolean) {
        _uiState.update { it.copy(emailError = hasError) }
    }


    private fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    private fun updatePasswordError(hasError: Boolean) {
        _uiState.update { it.copy(passwordError = hasError) }
    }

    private fun updatePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !_uiState.value.isPasswordVisible) }
    }


    private fun updateConfirmPassword(inputPassword: String) {
        _uiState.update { it.copy(confirmPassword = inputPassword) }
    }

    private fun updateConfirmPasswordError(hasError: Boolean) {
        _uiState.update { it.copy(confirmPasswordError = hasError) }
    }

    private fun updateConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible) }
    }




private fun register(name: String, email: String, validPassword: String, password: String) {

    viewModelScope.launch {

        registerUserUseCase(
            name = name,
            email=email,
            validPassword = validPassword,
            password = password
        ).collect { uiResourse ->
            when (uiResourse) {
                is UiResource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }

                is UiResource.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _effectFlow.emit(RegisterViewEffect.NavigateToLogin)//navigate to home

                }

                is UiResource.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = uiResourse.exception.message ?: "UnKnownError"
                    )
                    if (uiState.value.errorMessage != "") {
                        _effectFlow.emit(RegisterViewEffect.ShowSnackBar(uiState.value.errorMessage))
                    }
                }
            }

        }
    }

}

}
