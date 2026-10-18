package com.example.nasaspaceimages.presentation.searchspaceimages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaspaceimages.R
import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.usecase.SaveSpaceImageUseCase
import com.example.nasaspaceimages.domain.usecase.SearchSpaceImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSpaceImagesViewModel @Inject constructor(
    private val searchSpaceImagesUseCase: SearchSpaceImagesUseCase,
    private val saveSpaceImageUseCase: SaveSpaceImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchImagesUiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = Channel<Int>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    private val savingNasaIds = mutableSetOf<String>()
    private var activeQuery: String? = null
    private var activeFilters = SearchFilters()
    private var searchJob: Job? = null

    init {
        search()
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun onCenterChange(value: String) {
        _uiState.update { it.copy(center = value) }
    }

    fun onYearStartChange(value: String) {
        _uiState.update { it.copy(yearStart = value) }
    }

    fun onYearEndChange(value: String) {
        _uiState.update { it.copy(yearEnd = value) }
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(center = "", yearStart = "", yearEnd = "")
        }
    }

    fun search() {
        val state = _uiState.value
        val start = state.yearStart.trim()
        val end = state.yearEnd.trim()

        fun validYear(value: String) = value.isEmpty() ||
                (value.length == 4 &&
                        value.all(Char::isDigit) &&
                        value != "0000")

        if (!validYear(start) || !validYear(end)) {
            _uiState.update {
                it.copy(errorResId = R.string.invalid_year)
            }
            return
        }

        val startYear = start.toIntOrNull()
        val endYear = end.toIntOrNull()

        if (startYear != null && endYear != null && startYear > endYear) {
            _uiState.update {
                it.copy(errorResId = R.string.invalid_year_range)
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
                    errorResId = null,
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
                        errorResId = R.string.search_failed
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
                    if (saved) R.string.saved_successfully
                    else R.string.already_saved
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _messages.send(R.string.save_failed)
            } finally {
                savingNasaIds.remove(image.nasaId)
            }
        }
    }
}