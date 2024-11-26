package com.example.firebase_learn.presentition.register

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.versionedparcelable.ParcelField
import kotlinx.coroutines.launch
import java.time.format.TextStyle


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel(), onNavigateToLogin: () -> Unit) {
    //uiState
    val uiState = viewModel.uiState.collectAsState()


    //snackBarState
    val snackbarHostState = remember { SnackbarHostState() }
    var coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is RegisterViewEffect.ShowSnackBar -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            effect.message, duration = SnackbarDuration.Long
                        )
                    }
                }

                is RegisterViewEffect.NavigateToHome -> {
//                    onNavigateToLogin()  //not handeld becouse i don't need it in that sceen
                }

                is RegisterViewEffect.NavigateToLogin -> {
                    onNavigateToLogin()
                }
            }

        }

    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .fillMaxSize()
//                .background(Color.Red)
            ) {
                Text(
                    text = "Create a New Account",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = uiState.value.name,
                    onValueChange = { viewModel.handeIntent(RegisterViewIntent.UpdateName(it)) },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = uiState.value.email,
                    onValueChange = {
                        viewModel.handeIntent(RegisterViewIntent.UpdateEmail(it))
                    },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = uiState.value.phoneNumber,
                    onValueChange = {
                        viewModel.handeIntent(RegisterViewIntent.UpdatePhone(it))
                    },
                    label = { Text("Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                )
                OutlinedTextField(
                    value = uiState.value.password,
                    onValueChange = { viewModel.handeIntent(RegisterViewIntent.UpdatePassword(it)) },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(shape = RoundedCornerShape(5.dp), color = Color.Blue),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),// Set background color here

                    onClick = {
                        viewModel.handeIntent(
                            RegisterViewIntent.Register(
                                uiState.value.name,
                                uiState.value.email,
                                uiState.value.phoneNumber,
                                uiState.value.password
                            )
                        )

//                onNavigateToLogin() //navigation     handle that in launchedEffect
                    }) {
                    Text("Register(Create a new Account ) ")

                }
                Text(color = Color.Red, text = uiState.value.errorMessage)


            }//column
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.value.isLoading) {

                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }


}

@Preview
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(onNavigateToLogin = {})
}