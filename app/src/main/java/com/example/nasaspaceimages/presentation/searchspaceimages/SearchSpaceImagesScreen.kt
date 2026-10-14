package com.example.nasaspaceimages.presentation.searchspaceimages

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SearchSpaceImagesScreen (
    modifier: Modifier = Modifier,
    viewModel: SearchSpaceImagesViewModel = hiltViewModel()
    ) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        OutlinedTextField(
            value = uiState.query,
            onValueChange = { query -> viewModel.onQueryChange(query)},
            label = {Text("Поиск изображений")},
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.search()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("Найти") }

        if (uiState.isLoading){
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let{error -> Text(text = error)}

        LazyColumn{
            items(uiState.images){ image ->
                Text(
                    text = image.title,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}