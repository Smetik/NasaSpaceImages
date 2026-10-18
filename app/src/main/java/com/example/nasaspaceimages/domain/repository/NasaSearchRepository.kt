package com.example.nasaspaceimages.domain.repository

import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage

interface NasaSearchRepository {
    suspend fun searchImages(
        query: String?,
        page: Int,
        filters: SearchFilters
    ): SpaceImageSearchPage
}