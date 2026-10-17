package com.example.nasaspaceimages.presentation.searchspaceimages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.foundation.clickable
import com.example.nasaspaceimages.domain.model.SpaceImage

@Composable
fun SearchSpaceImagesScreen (
    onImageClick: (SpaceImage) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchSpaceImagesViewModel = hiltViewModel()
    ) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.messages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { query -> viewModel.onQueryChange(query) },
                label = { Text("Поиск изображений") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.search()
                },
                modifier = Modifier.padding(top = 8.dp)
            ) { Text("Найти") }

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.errorMessage != null -> {
                    Text(text = uiState.errorMessage!!)
                }

                uiState.hasSearched && uiState.images.isEmpty() -> {
                    Text("По вашему запросу ничего не найдено")
                }

                else -> {

                    LazyColumn (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ){
                        items(uiState.images) { image ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    AsyncImage(
                                        model = image.mediaUrl,
                                        contentDescription = image.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clickable {
                                                onImageClick(image)
                                            },
                                        contentScale = ContentScale.Crop
                                    )

                                    Text(
                                        text = image.title,
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .clickable {
                                                onImageClick(image)
                                            }
                                    )

                                    Button(
                                        onClick = {
                                            viewModel.saveImage(image)
                                        },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text("Сохранить")
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}