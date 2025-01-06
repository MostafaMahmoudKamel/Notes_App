package com.example.firebase_learn.presentition.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
//        topBar = {
//            TopAppBar(
//                title = { Text("NOTESAPP") },
//                navigationIcon = { Icon(Icons.Default.ArrowBack, null) },
//
//
////            CustomToolBar(
////                text = "NOTES APP",
////                imageVectorEnd = Icons.AutoMirrored.Filled.ExitToApp,
////                onIconClicked = { viewModel.handeIntent(HomeViewIntent.Logout) },
////                )
//
//                )
//        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
                title = { Text(text="Notes App", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.handeIntent(HomeViewIntent.SetExpand(expand = true)) }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options",
                            tint=Color.Black
                        )
                    }
                    // setting in toolBar such as Deleting all Notes
                    DropdownMenu(
                        expanded = uiState.value.expandSetting,//false
                        onDismissRequest = {viewModel.handeIntent(HomeViewIntent.SetExpand(expand = false)) },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.handeIntent(HomeViewIntent.SetExpand(expand = false));
                            viewModel.handeIntent(HomeViewIntent.ClearAllNotes)
                        }, text = { Text("Clear Notes") })
//                        DropdownMenuItem(onClick = { expanded = false }, text = { Text("Item2") })
                    }

                    IconButton(onClick = { viewModel.handeIntent(HomeViewIntent.Logout) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, null,
                            tint=Color.Black
                        )
                    }
                }
            )
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
                                viewModel.handeIntent(HomeViewIntent.GetAllNotes)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Image"
                                )
                            }
                        }
                    },
                    label = { Text(text = "Search Note") })

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
                                Row(modifier = Modifier.fillMaxWidth()) {

                                    Text(
                                        text = "TITLE :\n ${note.title}",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(16.dp).weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "EditIcon",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                Text(
                                    text = "DESCRIPTION :\n ${note.data}",
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


