import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.presentition.login.LoginUiEffect
import com.example.firebase_learn.presentition.login.LoginViewIntent
import com.example.firebase_learn.presentition.login.LoginViewModel
import com.example.firebase_learn.utils.isEmailValid
import com.example.firebase_learn.utils.isPasswordValid
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    oncNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // State
    val uiState = viewModel.uiState.collectAsState()
    //sanckBar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val requestFocus = remember { FocusRequester() }
    LaunchedEffect(viewModel) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.ShowSnackBar -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            effect.message,
                            duration = SnackbarDuration.Long
                        )
                    }
                }

                is LoginUiEffect.NavigateToHome -> { oncNavigateToHome() }
                is LoginUiEffect.NavigateToRegister -> { onNavigateToRegister()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }  // add SnackBarHost to
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center


            ) {

                Text(
                    "Login Screen",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 32.dp)
                )
                OutlinedTextField(
                    value = uiState.value.email,
                    onValueChange = {
                        viewModel.handeIntent(LoginViewIntent.UpdateEmail(it))
                        //check email Validation
                        viewModel.handeIntent(LoginViewIntent.UpdateEmailError(!Patterns.EMAIL_ADDRESS.matcher(it).matches()))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester = requestFocus)
                        //apply email error if i focus and doesn't enter any data
                        .onFocusChanged { if(it.isFocused){viewModel.handeIntent(LoginViewIntent.UpdateEmailError(true))} },
                    label = { Text("Email") },
                    enabled = !uiState.value.isLoading, // Disable when loading
                    isError = uiState.value.emailError,
                    singleLine = true

                )
                OutlinedTextField(
                    value = uiState.value.password,
                    onValueChange = {
                        viewModel.handeIntent(LoginViewIntent.UpdatePassword(it))
                        //check password Validation
                        viewModel.handeIntent(LoginViewIntent.UpdatePasswordError(!it.isPasswordValid()))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester =requestFocus )
                        //apply error if i focus on password and doesn't enter any data
                        .onFocusChanged { if(it.isFocused){viewModel.handeIntent(LoginViewIntent.UpdatePasswordError(true))} },
                    label = { Text("Password") },
                    isError = uiState.value.passwordError,
                    singleLine = true,
                    enabled = !uiState.value.isLoading // Disable when loading

                )
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    onClick = {
                        //check if All Filed is valid and doesn't have any error
                        if(!uiState.value.passwordError&&!uiState.value.emailError&&uiState.value.email!=""&&uiState.value.password!="") {
                            viewModel.handeIntent(
                                LoginViewIntent.Login(
                                    uiState.value.email,
                                    uiState.value.password
                                )
                            )
                        }else{
                            //apply error = true if any filed have an error
                            if(uiState.value.emailError||!(uiState.value.email).isEmailValid()){
                                viewModel.handeIntent(LoginViewIntent.UpdateEmailError(true))
                            }
                            if(uiState.value.passwordError||!(uiState.value.password).isPasswordValid()){
                                viewModel.handeIntent(LoginViewIntent.UpdatePasswordError(true))

                            }
                        }
                    },
                    enabled = !uiState.value.isLoading // Disable when loading

                ) {
                    Text("Login")
                }
                Text(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    text = "Register a New Account",
                    modifier = Modifier.clickable {
                        viewModel.handeIntent(LoginViewIntent.HandleNavigateToRegister)
                    }
                )

//                Text(color = Color.Red, text = uiState.value.errorMessage)


            }//column

            if (uiState.value.isLoading) {
//                Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)

                )
            }
//            }

        }//box
    }

}
