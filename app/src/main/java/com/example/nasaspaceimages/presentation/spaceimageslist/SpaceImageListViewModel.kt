package com.example.nasaspaceimages.presentation.spaceimageslist
import androidx.lifecycle.ViewModel
import com.example.nasaspaceimages.domain.usecase.SortSpaceImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.nasaspaceimages.domain.model.SortOption
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SpaceImageListViewModel @Inject constructor(
    private val sortSpaceImagesUseCase: SortSpaceImagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpaceImageListUiState())
    val uiState: StateFlow<SpaceImageListUiState> = _uiState.asStateFlow()

    init {
        loadImages()
    }

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

    fun changeSortOption(sortOption: SortOption) {
        _uiState.update {
            it.copy(sortOption = sortOption)
        }

        loadImages()
    }

    fun refreshImages() {
        loadImages()
    }
}