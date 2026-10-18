package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.nasaspaceimages.R
import com.example.nasaspaceimages.domain.model.SortOption
import com.example.nasaspaceimages.domain.model.SpaceImage

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
    onImageClick: (SpaceImage) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    viewModel: SpaceImageListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var imageToDelete by remember {
        mutableStateOf<SpaceImage?>(null)
    }

    var imageToEdit by remember {
        mutableStateOf<SpaceImage?>(null)
    }

    var sortMenuExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel) {
        viewModel.refreshImages()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            OutlinedButton(
                onClick = { sortMenuExpanded = true }
            ) {
                Text(stringResource(uiState.sortOption.labelRes()))
            }

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = {
                    sortMenuExpanded = false
                }
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(option.labelRes()))
                        },
                        onClick = {
                            if (option != uiState.sortOption) {
                                listState.requestScrollToItem(0)
                                viewModel.changeSortOption(option)
                            }
                            sortMenuExpanded = false
                        }
                    )
                }
            }
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.images.isEmpty() -> {
                Text(stringResource(R.string.empty_catalog))
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = uiState.images,
                        key = { it.id }
                    ) { image ->

                        SavedSpaceImageCard(
                            image = image,

                            onOpen = {
                                onImageClick(image)
                            },

                            onFavoriteClick = {
                                viewModel.toggleFavorite(image)
                            },

                            onEditClick = {
                                imageToEdit = image
                            },

                            onDeleteClick = {
                                imageToDelete = image
                            }
                        )
                    }
                }
            }
        }
    }

    imageToDelete?.let { image ->
        DeleteSpaceImageDialog(
            image = image,

            onConfirm = {
                viewModel.deleteImage(image.id)
                imageToDelete = null
            },

            onDismiss = {
                imageToDelete = null
            }
        )
    }

    imageToEdit?.let { image ->
        EditSpaceImageDialog(
            image = image,

            onSave = { title, comment ->
                viewModel.editImage(
                    image = image,
                    title = title,
                    comment = comment
                )

                imageToEdit = null
            },

            onDismiss = {
                imageToEdit = null
            }
        )
    }
}