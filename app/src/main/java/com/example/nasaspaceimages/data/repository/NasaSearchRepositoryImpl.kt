package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.data.remote.SpaceImageRemoteDataSource
import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import com.example.nasaspaceimages.domain.repository.NasaSearchRepository
import javax.inject.Inject

class NasaSearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpaceImageRemoteDataSource
) : NasaSearchRepository {

    override suspend fun searchImages(
        query: String?,
        page: Int,
        filters: SearchFilters
    ): SpaceImageSearchPage {
        return remoteDataSource.searchImages(
            query = query,
            page = page,
            filters = filters
        )
    }
}