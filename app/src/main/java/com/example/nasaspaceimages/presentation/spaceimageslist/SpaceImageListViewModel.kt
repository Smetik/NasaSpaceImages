package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaspaceimages.domain.model.SortOption
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.usecase.DeleteSpaceImageUseCase
import com.example.nasaspaceimages.domain.usecase.SortSpaceImagesUseCase
import com.example.nasaspaceimages.domain.usecase.UpdateSpaceImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpaceImageListViewModel @Inject constructor(
    private val sortSpaceImagesUseCase: SortSpaceImagesUseCase,
    private val updateSpaceImageUseCase: UpdateSpaceImageUseCase,
    private val deleteSpaceImageUseCase: DeleteSpaceImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpaceImageListUiState())
    val uiState = _uiState.asStateFlow()

    private fun loadImages() = viewModelScope.launch {
        _uiState.update {
            it.copy(isLoading = true)
        }

        val images = sortSpaceImagesUseCase(
            _uiState.value.sortOption
        )

        _uiState.update {
            it.copy(
                images = images,
                isLoading = false
            )
        }
    }

    fun refreshImages() {
        loadImages()
    }

    fun changeSortOption(option: SortOption) {
        _uiState.update {
            it.copy(sortOption = option)
        }
        loadImages()
    }

    fun toggleFavorite(image: SpaceImage) {
        viewModelScope.launch {
            updateSpaceImageUseCase(
                image.copy(isFavorite = !image.isFavorite)
            )
            refreshImages()
        }
    }

    fun deleteImage(imageId: Long) {
        viewModelScope.launch {
            deleteSpaceImageUseCase(imageId)
            refreshImages()
        }
    }

    fun editImage(
        image: SpaceImage,
        title: String,
        comment: String
    ) {
        if (title.isBlank()) return

        viewModelScope.launch {
            updateSpaceImageUseCase(
                image.copy(
                    title = title.trim(),
                    comment = comment.trim()
                )
            )
            refreshImages()
        }
    }
}