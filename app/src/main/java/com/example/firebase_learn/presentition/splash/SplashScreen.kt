package com.example.firebase_learn.presentition.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var uiState = viewModel.uiState.collectAsState()
    var coroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is SplashViewEffect.NavigateToHome -> {onNavigateToHome()
                }
                is SplashViewEffect.NavigateToLogin -> {
                    onNavigateToLogin()

                }
            }
        }
    }
    // Send the intent to check user status after a delay
    LaunchedEffect(viewModel) {
        coroutineScope.launch {
            delay(2000)
            viewModel.handleIntent(SplashViewIntent.CheckUserStatus)

        }

    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),

        ) {
        Image(
            painter = painterResource(R.drawable.note_img),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = "Welcome to Firebase Notes!",
            fontSize = 24.sp,
            color = Color.Blue,
            modifier = Modifier.padding(16.dp)
        )

    }

}

//@Composable
//fun SplashScreenContent() {
//
//    Column {
//        Text("Welcome to My NOte App")
//    }
//}

//@Preview
//@Composable
//fun SplashScreenPreview() {
//    SplashScreenContent()
//}