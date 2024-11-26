package com.example.firebase_learn.presentition.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learn.R
import com.example.firebase_learn.presentition.component.CustomToolBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToUpdate: (noteId: String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.getData()
//        viewModel.getAllNOtes()
    }


    LaunchedEffect(viewModel) {
        //collect effect
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is HomeViewEffect.ShowSnackBar -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            effect.message, duration = SnackbarDuration.Short
                        )
                    }
                }

                is HomeViewEffect.NavigateToLogin -> {
                    onNavigateToLogin()
                }

                is HomeViewEffect.NavigateToAdd -> {
                    onNavigateToAdd()
                }
            }

        }
    }
    Scaffold(
        topBar = {
            CustomToolBar(
                text = "NOTES APP",
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                onIconClicked = { viewModel.handeIntent(HomeViewIntent.Logout) })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.handeIntent(HomeViewIntent.NavigateToAdd)
            }, contentColor = Color.White, containerColor = Color.Blue) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {

                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    value = uiState.value.search,
                    onValueChange = {
                        viewModel.handeIntent(HomeViewIntent.SetSearchTxt(search = it))
                        viewModel.handeIntent(HomeViewIntent.SearchQuery)
                    },
                    trailingIcon = {
                        if (uiState.value.search != "") {
                            IconButton(onClick = {
                                viewModel.handeIntent(HomeViewIntent.ClearSearchTxt)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Image"
                                )
                            }
                        }
                    })

                if (uiState.value.lNotes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {

                        Text(
                            text = "No notes found",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Red
                        )
                    }
                } else {
                    LazyColumn {

                        items(uiState.value.lNotes) { note ->
                            Card(
                                modifier = Modifier
                                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.Cyan),
                                onClick = {
                                    onNavigateToUpdate(note.noteId) //pass data to update screen
                                }
                            ) {
                                Text(
                                    text = "title is :\n ${note.title}",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Text(
                                    text = "data is :\n ${note.data}",
                                    color = Color.Black,
                                    modifier = Modifier.padding(16.dp)
                                )

                            }
                        }

                    }
                }
            }
            if (uiState.value.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = .5f))
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        } //Box
    }//Scaffold
}//homeScreen


