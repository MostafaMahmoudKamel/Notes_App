package com.example.firebase_learn.presentition.add

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.data.model.Note
import kotlinx.coroutines.launch

@Composable
fun AddScreen(viewModel: AddViewModel = hiltViewModel(), onNavigateToHome: () -> Unit) {


    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is AddViewEffect.ShowSnackBar -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            effect.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is AddViewEffect.NavigateToHome -> {
                    onNavigateToHome()
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState),
        ) {
            Text(
                text = "Add new Note",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = uiState.value.title,
                onValueChange = {
                    viewModel.handeIntent(AddViewIntent.SetTitle(it))
                },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = uiState.value.data,
                onValueChange = {
                    viewModel.handeIntent(AddViewIntent.SetData(it))
                },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)


            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),

                onClick = {
                    viewModel.handeIntent(
                        AddViewIntent.AddNote(
                            Note(
                                title = uiState.value.title, data = uiState.value.data
                            )
                        )
                    )
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.value.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center),

                            color = MaterialTheme.colorScheme.secondary

                        )
                    } else {
                        Text("AddNote", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}