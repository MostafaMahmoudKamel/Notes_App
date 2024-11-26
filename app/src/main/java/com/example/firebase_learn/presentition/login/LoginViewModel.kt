package com.example.firebase_learn.presentition.login


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.data.sharedPref.SharedPrefApp
import com.example.firebase_learn.domain.repository.UserRepository
import com.example.firebase_learn.domain.usecase.userUseCase.GetFireStoreDataUserUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.LogoutUserUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.RegisterUserUseCase
import com.example.firebase_learn.domain.usecase.userUseCase.SignInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class LoginViewModel @Inject constructor(
//    private var userRepository: UserRepository,
    private var signInUserUseCase: SignInUserUseCase,
//    private var sharedPrefApp: SharedPrefApp,

    ) : ViewModel() {

    private var _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    private var _effectFlow = MutableSharedFlow<LoginUiEffect>()
    var effectFlow: SharedFlow<LoginUiEffect> = _effectFlow.asSharedFlow()


    fun handeIntent(intent: LoginViewIntent) {
        when (intent) {
            is LoginViewIntent.UpdateEmail -> {
                updateEmail(intent.email)
            }

            is LoginViewIntent.UpdatePassword -> {
                updatePassword(intent.password)
            }

            is LoginViewIntent.Login -> {
                login(intent.email, intent.password)
            }

            is LoginViewIntent.HandleNavigateToRegister -> {
                handleRegisterNavigation()
            }

        }

    }

    private fun updateEmail(inputEmail: String) {
        _uiState.value = _uiState.value.copy(email = inputEmail)
    }

    private fun handleRegisterNavigation() {
        viewModelScope.launch {
            _effectFlow.emit(LoginUiEffect.NavigateToRegister)
        }
    }

    private fun updatePassword(inputPassword: String) {
        _uiState.value = _uiState.value.copy(password = inputPassword)
    }


    private fun login(email: String, password: String) {//login==signIN
        viewModelScope.launch {
            signInUserUseCase(email = email, password = password).collect { uiResourse ->

                when (uiResourse) {
                    is UiResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is UiResource.Success -> {
                        _effectFlow.emit(LoginUiEffect.ShowSnackBar("successNavigate"))
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        //Shared preferences   -->put handle of it in repository
                        _effectFlow.emit(LoginUiEffect.NavigateToHome)//navigate to home //navigate effect
                    }

                    is UiResource.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = uiResourse.exception.message ?: "unKnownError"
                        )
                        _effectFlow.emit(LoginUiEffect.ShowSnackBar("failNavigate"))

                    }
                }
            }
        }
    }


}
