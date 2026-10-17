package com.example.nasaspaceimages.data.remote

import com.example.nasaspaceimages.data.mapper.toDomainOrNull
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import javax.inject.Inject

class SpaceImageRemoteDataSource @Inject constructor(private val api: NasaImagesApi) {
    suspend fun searchImages(
        query: String?,
        page: Int
    ): SpaceImageSearchPage {
        val response = api.searchImages(
            query = query,
            page = page
        )

        return SpaceImageSearchPage(
            images = response.collection.items
                .mapNotNull { it.toDomainOrNull() },

            hasNextPage = response.collection.links.any {
                it.rel == "next"
            }
        )
    }
}