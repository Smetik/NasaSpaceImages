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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState

@Composable
fun SearchSpaceImagesScreen (
    onImageClick: (SpaceImage) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    viewModel: SearchSpaceImagesViewModel = hiltViewModel()
    ) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var centerMenuExpanded by remember {
        mutableStateOf(false)
    }

    var filtersExpanded by rememberSaveable {
        mutableStateOf(false)
    }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = viewModel::search,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Найти")
                }

                OutlinedButton(
                    onClick = { filtersExpanded = !filtersExpanded },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Tune, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Фильтры")
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
                            label = { Text("Год от") },
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
                            label = { Text("Год до") },
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
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { centerMenuExpanded = true },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text(
                                    if (uiState.center.isBlank())
                                        "Все центры"
                                    else
                                        "Центр: ${uiState.center}"
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
                                            Text(center.ifBlank { "Все центры" })
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
                            Text("Сбросить")
                        }
                    }
                }
            }

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
                        state = listState,
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

            if (uiState.hasSearched && uiState.errorMessage == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = viewModel::previousPage,
                        enabled = uiState.page > 1 && !uiState.isLoading
                    ) {
                        Text("Назад")
                    }

                    Text("Страница ${uiState.page}")

                    OutlinedButton(
                        onClick = viewModel::nextPage,
                        enabled = uiState.hasNextPage && !uiState.isLoading
                    ) {
                        Text("Вперёд")
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