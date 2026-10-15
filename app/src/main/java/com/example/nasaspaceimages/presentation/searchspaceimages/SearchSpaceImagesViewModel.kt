package com.example.nasaspaceimages.presentation.searchspaceimages

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.usecase.SaveSpaceImageUseCase
import com.example.nasaspaceimages.domain.usecase.SearchSpaceImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SearchSpaceImagesViewModel @Inject constructor(
    private val searchSpaceImagesUseCase: SearchSpaceImagesUseCase,
    private val saveSpaceImageUseCase: SaveSpaceImageUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SearchImagesUiState())
    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()
    val uiState: StateFlow<SearchImagesUiState> = _uiState.asStateFlow()


    init {
        search()
    }

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
                    images = emptyList(),
                    isLoading = true,
                    errorMessage = null,
                    hasSearched = false
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
                        isLoading = false,
                        hasSearched = true
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

    fun saveImage(image: SpaceImage) {
        viewModelScope.launch {
            try {
                saveSpaceImageUseCase(image)
                _messages.send("Данные сохранены")

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _messages.send("Не удалось сохранить изображение")
            }
        }
    }
}