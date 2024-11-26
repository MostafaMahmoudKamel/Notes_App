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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.presentition.login.LoginUiEffect
import com.example.firebase_learn.presentition.login.LoginViewIntent
import com.example.firebase_learn.presentition.login.LoginViewModel
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

    var context = LocalContext.current

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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    label = { Text("Email") },
                    enabled = !uiState.value.isLoading // Disable when loading

                )
                OutlinedTextField(
                    value = uiState.value.password,
                    onValueChange = {
                        viewModel.handeIntent(LoginViewIntent.UpdatePassword(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    label = { Text("Password") },

                    enabled = !uiState.value.isLoading // Disable when loading

                )
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    onClick = {
                        viewModel.handeIntent(
                            LoginViewIntent.Login(
                                uiState.value.email,
                                uiState.value.password
                            )
                        )
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

                Text(color = Color.Red, text = uiState.value.errorMessage)


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
