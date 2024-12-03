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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.utils.isEmailValid
import com.example.firebase_learn.utils.isNameValid
import com.example.firebase_learn.utils.isPasswordMatches
import com.example.firebase_learn.utils.isPasswordValid
import kotlinx.coroutines.launch


@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel(), onNavigateToLogin: () -> Unit) {
    //uiState
    val uiState = viewModel.uiState.collectAsState()

    val focusRequester = remember { FocusRequester() }


    //snackBarState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
            ) {
                Text(
                    text = "Create a New Account",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = uiState.value.name,
                    onValueChange = {
                        //update name
                        viewModel.handeIntent(RegisterViewIntent.UpdateName(it))
                        //validate name and update error
                        viewModel.handeIntent(RegisterViewIntent.UpdateNameError(!it.isNameValid()))
                    },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                viewModel.handeIntent(
                                    RegisterViewIntent.UpdateNameError(
                                        true
                                    )
                                )
                            }
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = uiState.value.nameError
                )
                if (uiState.value.nameError) {
                    Text(
                        text = "Name letters must be 5 or more ", modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp), color = Color.Red
                    )
                } else {
                    Text("")
                }

                OutlinedTextField(
                    value = uiState.value.email,
                    onValueChange = {
                        viewModel.handeIntent(RegisterViewIntent.UpdateEmail(it))
                        viewModel.handeIntent(RegisterViewIntent.UpdateEmailError(hasError = !it.isEmailValid()))
                    },
                    isError = uiState.value.emailError,
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                viewModel.handeIntent(
                                    RegisterViewIntent.UpdateEmailError(
                                        hasError = true
                                    )
                                )

                            }
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )

                )
                if (uiState.value.emailError) {
                    Text(
                        text = "email must be valid ", modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp), color = Color.Red
                    )
                } else {
                    Text("")
                }

                OutlinedTextField(
                    value = uiState.value.password,
                    onValueChange = {
                        viewModel.handeIntent(RegisterViewIntent.UpdatePassword(it))
                        //checkValidation of password
                        viewModel.handeIntent(RegisterViewIntent.UpdatePasswordError(hasError = !it.isPasswordValid()))
                    },
                    label = { Text("password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester)//handle focus
                        .onFocusChanged {
                            if (it.isFocused) {
                                //apply error password
                                viewModel.handeIntent(
                                    RegisterViewIntent.UpdatePasswordError(true)
                                )
                            }
                        },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.handeIntent(RegisterViewIntent.UpdatePasswordVisisbility) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    isError = uiState.value.passwordError,
                    visualTransformation = if (uiState.value.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

                )
                if (uiState.value.passwordError) {
                    Text(
                        text = "password must consist of 6 digits ", modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp), color = Color.Red
                    )
                } else {
                    Text("")
                }

                OutlinedTextField(
                    value = uiState.value.confirmPassword,
                    onValueChange = {
                        //update confirm password
                        viewModel.handeIntent(RegisterViewIntent.UpdateConfirmPassword(it))
                        //update confirm password validation and error   and passwordMatches
                        viewModel.handeIntent(
                            RegisterViewIntent.UpdateConfirmPasswordError(
                                !(it.isPasswordValid() && it.isPasswordMatches(
                                    uiState.value.password
                                ))
                            )
                        )
                    },
                    label = { Text("Valid-Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester)
                        //apply error when isFocus to confirmPassword
                        .onFocusChanged {
                            if (it.isFocused) {
                                //apply error confirmPassword
                                viewModel.handeIntent(
                                    RegisterViewIntent.UpdateConfirmPasswordError(
                                        true
                                    )
                                )
                            }
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.handeIntent(RegisterViewIntent.UpdateConfirmPasswordVisisbality) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }

                    },
                    isError = uiState.value.confirmPasswordError,
                    visualTransformation = if (uiState.value.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),

                    )
                if (uiState.value.confirmPasswordError) {
                    if (uiState.value.confirmPassword.length < 6) {
                        Text(
                            text = "password must consist of 6 digits ", modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp), color = Color.Red
                        )
                    } else {
                        Text(
                            text = "password doesn't match confirm Password",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            color = Color.Red
                        )
                    }
                } else {
                    Text("")

                }

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(shape = RoundedCornerShape(5.dp), color = Color.Blue),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),// Set background color here

                    onClick = {
                        if (uiState.value.name.isNameValid() && uiState.value.email.isEmailValid() &&
                            uiState.value.password.isPasswordValid() && uiState.value.confirmPassword.isPasswordValid()
                            && uiState.value.password.isPasswordMatches(uiState.value.confirmPassword)
                        ) {
                            viewModel.handeIntent(
                                RegisterViewIntent.Register(
                                    uiState.value.name,
                                    uiState.value.email,
                                    uiState.value.password,
                                    uiState.value.confirmPassword
                                )
                            )
                        } else {
                            if (uiState.value.nameError || uiState.value.name == "") {
                                viewModel.handeIntent(RegisterViewIntent.UpdateNameError(true))
                            }
                            if (uiState.value.emailError || uiState.value.email == "") {
                                viewModel.handeIntent(RegisterViewIntent.UpdateEmailError(true))
                            }
                            if (uiState.value.passwordError || uiState.value.password == "") {
                                viewModel.handeIntent(
                                    RegisterViewIntent.UpdatePasswordError(
                                        true
                                    )
                                )
                            }
                            if (uiState.value.confirmPasswordError || uiState.value.confirmPassword == "") {
                                viewModel.handeIntent(
                                    RegisterViewIntent.UpdateConfirmPasswordError(
                                        true
                                    )
                                )
                            }

                        }


//                onNavigateToLogin() //navigation     handle that in launchedEffect
                    }) {
                    Text("Register(Create a new Account ) ")

                }
//                Text(color = Color.Red, text = uiState.value.errorMessage)


            }//column
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.value.isLoading) {

                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }


}
//}

@Preview
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(onNavigateToLogin = {})
}