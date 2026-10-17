package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class SearchSpaceImagesUseCase @Inject constructor(private val repository: SpaceImageRepository){

    suspend operator fun invoke(
        query: String?,
        page: Int,
        filters: SearchFilters
    ): SpaceImageSearchPage {
        return repository.searchImages(
            query = query,
            page = page.coerceAtLeast(1),
            filters = filters
        )
    }
}