package com.example.firebase_learn.presentition.update

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.presentition.component.CustomToolBar

@Composable
fun UpdateScreen(
    viewModel: UpdateViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    noteId: String
) {


    val uiState = viewModel.uiState.collectAsState()

    //call when updateScreen is opened
    LaunchedEffect(viewModel) {
        viewModel.getDataOfNote(noteId = noteId)
    }
    LaunchedEffect(viewModel) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is UpdateViewEffect.NavigateToHome -> onNavigateToHome()
            }

        }
    }
    Scaffold(
        topBar = {
            CustomToolBar(
                text = "UpdateScreen",
                imageVector = Icons.Default.Delete,
                onIconClicked = {
                    viewModel.handleIntent(UpdateViewIntent.DeleteNote(noteId))
                }
            )
        }
    ) {paddingTopBar->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(paddingTopBar)
                    .fillMaxSize()
                    .systemBarsPadding(),
            ) {



                OutlinedTextField(
                    value = uiState.value.title,
                    onValueChange = {
                        viewModel.handleIntent(UpdateViewIntent.SetTitle(it))
                    },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = uiState.value.data,
                    onValueChange = {
                        viewModel.handleIntent(UpdateViewIntent.SetData(it))
                    },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)


                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),


                    onClick = {
                        viewModel.handleIntent(
                            UpdateViewIntent.UpdateNote(
                                Note(
                                    title = uiState.value.title,
                                    data = uiState.value.data,
                                    noteId = noteId,
                                    userId = uiState.value.userId
                                )
                            )
                        )
                    }
                ) {
                    Text("UpdateNOte")
                }


            }
            if (uiState.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

