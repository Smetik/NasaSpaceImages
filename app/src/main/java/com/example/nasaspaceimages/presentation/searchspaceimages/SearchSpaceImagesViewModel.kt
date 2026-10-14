package com.example.nasaspaceimages.presentation.searchspaceimages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaspaceimages.domain.usecase.SearchSpaceImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSpaceImagesViewModel @Inject constructor(
    private val searchSpaceImagesUseCase: SearchSpaceImagesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SearchImagesUiState())
    val uiState: StateFlow<SearchImagesUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String){
        _uiState.update{
            it.copy(
               query = query
            )
        }
    }

    fun search() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val images = searchSpaceImagesUseCase(
                    query = _uiState.value.query.ifBlank { null },
                    page = 1
                )

                _uiState.update {
                    it.copy(
                        images = images,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Не удалось загрузить изображения"
                    )
                }
            }
        }
    }
}