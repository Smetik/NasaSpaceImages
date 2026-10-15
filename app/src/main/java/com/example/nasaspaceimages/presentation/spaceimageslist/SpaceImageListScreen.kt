package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

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
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AsyncImage(
                            model = image.mediaUrl,
                            contentDescription = image.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = image.title,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.toggleFavorite(image)
                            }
                        ) {
                            Text(
                                if (image.isFavorite) {
                                    "★ В избранном"
                                } else {
                                    "☆ В избранное"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}