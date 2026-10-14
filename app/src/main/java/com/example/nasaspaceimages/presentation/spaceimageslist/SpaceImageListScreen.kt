package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SpaceImageListScreen(
    modifier: Modifier = Modifier,
    viewModel: SpaceImageListViewModel = hiltViewModel())
{
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.refreshImages()
    }

    Column(modifier = modifier) {
        if (uiState.isLoading) {
            CircularProgressIndicator()

        } else if (uiState.images.isEmpty()) {
            Text("Пока нет сохранённых изображений")

        } else {
            LazyColumn {
                items(uiState.images) { image ->
                    Text(text = image.title)
                }
            }
        }
    }
}