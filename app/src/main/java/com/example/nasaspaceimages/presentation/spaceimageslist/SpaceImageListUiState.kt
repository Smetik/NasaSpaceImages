package com.example.nasaspaceimages.presentation.spaceimageslist

import com.example.nasaspaceimages.domain.model.SortOption
import com.example.nasaspaceimages.domain.model.SpaceImage

data class SpaceImageListUiState(
    val images: List<SpaceImage> = emptyList(),
    val isLoading: Boolean = false,
    val sortOption: SortOption = SortOption.DATE_CREATED_NEWEST
)
