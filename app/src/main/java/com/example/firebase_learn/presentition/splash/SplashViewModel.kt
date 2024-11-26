package com.example.firebase_learn.presentition.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learn.data.sharedPref.SharedPrefApp
import com.example.firebase_learn.utils.SharedPrefKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private var sharedPrefApp: SharedPrefApp
) : ViewModel() {

    private var _uiState = MutableStateFlow(SplashUiState())
    var uiState = _uiState.asStateFlow()

    private var _effectFlow = MutableSharedFlow<SplashViewEffect>()
    var effectFlow = _effectFlow.asSharedFlow()

    fun handleIntent(intent: SplashViewIntent) {
        when (intent) {
            is SplashViewIntent.CheckUserStatus -> {checkUserStatus()}
        }
    }

    private fun checkUserStatus() {
        viewModelScope.launch {
            val isLogin = sharedPrefApp.getBoolean(SharedPrefKeys.isLogin, false)

            if (isLogin) {
                _effectFlow.emit(SplashViewEffect.NavigateToHome)

            } else {
                _effectFlow.emit(SplashViewEffect.NavigateToLogin)

            }
        }
    }
}