package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nasaspaceimages.domain.model.SortOption
import com.example.nasaspaceimages.domain.model.SpaceImage
import androidx.compose.ui.res.stringResource
import com.example.nasaspaceimages.R


@StringRes
private fun SortOption.labelRes(): Int = when (this) {
    SortOption.TITLE_ASC -> R.string.sort_title_asc
    SortOption.TITLE_DESC -> R.string.sort_title_desc
    SortOption.DATE_CREATED_NEWEST -> R.string.sort_date_newest
    SortOption.DATE_CREATED_OLDEST -> R.string.sort_date_oldest
    SortOption.FAVORITE_FIRST -> R.string.sort_favorite_first
    SortOption.FAVORITE_LAST -> R.string.sort_favorite_last
}

@Composable
fun SpaceImageListScreen(
    modifier: Modifier = Modifier,
    viewModel: SpaceImageListViewModel = hiltViewModel())
{
    val uiState by viewModel.uiState.collectAsState()
    var imageToDelete by remember {
        mutableStateOf<SpaceImage?>(null)
    }

    LaunchedEffect(viewModel) {
        viewModel.refreshImages()
    }

    var imageToEdit by remember { mutableStateOf<SpaceImage?>(null) }
    var editedTitle by remember { mutableStateOf("") }
    var editedComment by remember { mutableStateOf("") }

    var sortMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        Box {
            OutlinedButton(
                onClick = { sortMenuExpanded = true }
            ) {
                Text(stringResource(uiState.sortOption.labelRes()))
            }

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false }
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(stringResource(option.labelRes())) },
                        onClick = {
                            viewModel.changeSortOption(option)
                            sortMenuExpanded = false
                        }
                    )
                }
            }
        }

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

                        if (image.comment.isNotBlank()) {
                            Text(
                                text = image.comment,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

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

                        Button(
                            onClick = {
                                imageToEdit = image
                                editedTitle = image.title
                                editedComment = image.comment
                            }
                        ) {
                            Text("Редактировать")
                        }

                        Button(
                            onClick = {
                                imageToDelete = image
                            }
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            }

            imageToDelete?.let { image ->
                AlertDialog(
                    onDismissRequest = {
                        imageToDelete = null
                    },
                    title = {
                        Text("Удалить фотографию?")
                    },
                    text = {
                        Text("«${image.title}» будет удалена из каталога.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteImage(image.id)
                                imageToDelete = null
                            }
                        ) {
                            Text("Удалить")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                imageToDelete = null
                            }
                        ) {
                            Text("Отмена")
                        }
                    }
                )
            }

            imageToEdit?.let { image ->
                AlertDialog(
                    onDismissRequest = {
                        imageToEdit = null
                    },
                    title = {
                        Text("Редактирование фотографии")
                    },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = editedTitle,
                                onValueChange = { editedTitle = it },
                                label = { Text("Название") }
                            )

                            OutlinedTextField(
                                value = editedComment,
                                onValueChange = { editedComment = it },
                                label = { Text("Комментарий") },
                                modifier = Modifier.padding(top = 8.dp),
                                minLines = 3
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.editImage(
                                    image,
                                    editedTitle,
                                    editedComment
                                )
                                imageToEdit = null
                            },
                            enabled = editedTitle.isNotBlank()
                        ) {
                            Text("Сохранить")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                imageToEdit = null
                            }
                        ) {
                            Text("Отмена")
                        }
                    }
                )
            }
        }
    }
}