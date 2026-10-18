package com.example.nasaspaceimages.presentation.searchspaceimages

import androidx.annotation.StringRes
import com.example.nasaspaceimages.domain.model.SpaceImage

data class SearchImagesUiState(
    val query: String = "",
    val images: List<SpaceImage> = emptyList(),
    val isLoading: Boolean = false,
    @StringRes val errorResId: Int? = null,
    val hasSearched: Boolean = false,
    val page: Int = 1,
    val hasNextPage: Boolean = false,
    val center: String = "",
    val yearStart: String = "",
    val yearEnd: String = ""
)