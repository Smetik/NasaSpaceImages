package com.example.nasaspaceimages.presentation.searchspaceimages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import com.example.nasaspaceimages.R
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.presentation.components.SpaceImageCard

@Composable
fun SearchSpaceImagesScreen(
    onImageClick: (SpaceImage) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    viewModel: SearchSpaceImagesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val resources = LocalResources.current

    var filtersExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var centerMenuExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel, resources) {
        viewModel.messages.collect { messageRes ->
            snackbarHostState.showSnackbar(
                resources.getString(messageRes)
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                label = {
                    Text(stringResource(R.string.search_hint))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = viewModel::search,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(stringResource(R.string.search))
                }

                OutlinedButton(
                    onClick = {
                        filtersExpanded = !filtersExpanded
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Tune,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(stringResource(R.string.filters))
                }
            }

            if (filtersExpanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.yearStart,
                            onValueChange = viewModel::onYearStartChange,
                            label = {
                                Text(stringResource(R.string.year_from))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = uiState.yearEnd,
                            onValueChange = viewModel::onYearEndChange,
                            label = {
                                Text(stringResource(R.string.year_to))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    centerMenuExpanded = true
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text(
                                    if (uiState.center.isBlank()) {
                                        stringResource(R.string.all_centers)
                                    } else {
                                        stringResource(
                                            R.string.selected_center,
                                            uiState.center
                                        )
                                    }
                                )
                            }

                            DropdownMenu(
                                expanded = centerMenuExpanded,
                                onDismissRequest = {
                                    centerMenuExpanded = false
                                }
                            ) {
                                listOf(
                                    "", "JPL", "JSC", "KSC",
                                    "GSFC", "MSFC", "ARC", "GRC", "HQ"
                                ).forEach { center ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                if (center.isBlank()) {
                                                    stringResource(R.string.all_centers)
                                                } else {
                                                    center
                                                }
                                            )
                                        },
                                        onClick = {
                                            viewModel.onCenterChange(center)
                                            centerMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = viewModel::resetFilters,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(stringResource(R.string.reset_filters))
                        }
                    }
                }
            }

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.errorResId != null -> {
                    val errorResId = uiState.errorResId

                    if (errorResId != null) {
                        Text(stringResource(errorResId))
                    }
                }

                uiState.hasSearched && uiState.images.isEmpty() -> {
                    Text(stringResource(R.string.nothing_found))
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(
                            items = uiState.images,
                            key = { it.nasaId }
                        ) { image ->

                            SpaceImageCard(
                                image = image,
                                onOpen = {
                                    onImageClick(image)
                                },
                                modifier = Modifier.padding(
                                    vertical = 8.dp
                                ),
                                bottomContent = {
                                    Button(
                                        onClick = {
                                            viewModel.saveImage(image)
                                        }
                                    ) {
                                        Text(stringResource(R.string.save))
                                    }
                                }
                            )
                        }
                    }
                }
            }

            if (
                uiState.hasSearched &&
                uiState.errorResId == null
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = viewModel::previousPage,
                        enabled = uiState.page > 1 && !uiState.isLoading
                    ) {
                        Text(stringResource(R.string.back))
                    }

                    Text(
                        stringResource(
                            R.string.page_number,
                            uiState.page
                        )
                    )

                    OutlinedButton(
                        onClick = viewModel::nextPage,
                        enabled = uiState.hasNextPage && !uiState.isLoading
                    ) {
                        Text(stringResource(R.string.next))
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