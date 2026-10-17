package com.example.nasaspaceimages.presentation.searchspaceimages

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
import kotlinx.coroutines.Job
import com.example.nasaspaceimages.domain.model.SearchFilters

@HiltViewModel
class SearchSpaceImagesViewModel @Inject constructor(
    private val searchSpaceImagesUseCase: SearchSpaceImagesUseCase,
    private val saveSpaceImageUseCase: SaveSpaceImageUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SearchImagesUiState())
    private val _messages = Channel<String>(Channel.BUFFERED)

    private val savingNasaIds = mutableSetOf<String>()

    private var activeQuery: String? = null
    private var searchJob: Job? = null

    private var activeFilters = SearchFilters()
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
        val state = _uiState.value

        val start = state.yearStart.trim()
        val end = state.yearEnd.trim()

        fun isValidYear(value: String): Boolean {
            return value.isEmpty() ||
                    (value.length == 4 &&
                            value.all { it.isDigit() } &&
                            value != "0000")
        }

        if (!isValidYear(start) || !isValidYear(end)) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Введите год в формате ГГГГ"
                )
            }
            return
        }

        val startYear = start.toIntOrNull()
        val endYear = end.toIntOrNull()

        if (startYear != null &&
            endYear != null &&
            startYear > endYear
        ) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Начальный год больше конечного"
                )
            }
            return
        }

        activeQuery = state.query.trim().ifBlank { null }

        activeFilters = SearchFilters(
            center = state.center.ifBlank { null },
            yearStart = startYear,
            yearEnd = endYear
        )

        loadPage(1)
    }

    fun nextPage() {
        if (searchJob?.isActive == true) return

        val state = _uiState.value

        if (state.hasNextPage) {
            loadPage(state.page + 1)
        }
    }

    fun previousPage() {
        if (searchJob?.isActive == true) return

        val state = _uiState.value

        if (state.page > 1) {
            loadPage(state.page - 1)
        }
    }

    private fun loadPage(page: Int) {
        searchJob?.cancel()

        val query = activeQuery
        val filters = activeFilters

        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    images = emptyList(),
                    isLoading = true,
                    errorMessage = null,
                    hasSearched = false
                )
            }

            try {
                val result = searchSpaceImagesUseCase(
                    query = query,
                    page = page,
                    filters = filters
                )

                _uiState.update {
                    it.copy(
                        images = result.images,
                        page = page,
                        hasNextPage = result.hasNextPage,
                        isLoading = false,
                        hasSearched = true
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        images = emptyList(),
                        page = page,
                        hasNextPage = false,
                        isLoading = false,
                        hasSearched = true,
                        errorMessage = e.message
                            ?: "Не удалось загрузить изображения"
                    )
                }
            }
        }
    }

    fun saveImage(image: SpaceImage) {
        if (!savingNasaIds.add(image.nasaId)) return

        viewModelScope.launch {
            try {
                val saved = saveSpaceImageUseCase(image)

                _messages.send(
                    if (saved) "Изображение сохранено"
                    else "Изображение уже сохранено"
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _messages.send("Не удалось сохранить изображение")
            } finally {
                savingNasaIds.remove(image.nasaId)
            }
        }
    }

    fun onCenterChange(center: String) {
        _uiState.update { it.copy(center = center) }
    }

    fun onYearStartChange(year: String) {
        _uiState.update { it.copy(yearStart = year) }
    }

    fun onYearEndChange(year: String) {
        _uiState.update { it.copy(yearEnd = year) }
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                center = "",
                yearStart = "",
                yearEnd = ""
            )
        }
    }

}