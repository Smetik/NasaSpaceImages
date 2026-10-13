package com.example.nasaspaceimages.data.remote

import com.example.nasaspaceimages.data.mapper.toDomainOrNull
import com.example.nasaspaceimages.domain.model.SpaceImage
import javax.inject.Inject

class SpaceImageRemoteDataSource @Inject constructor(private val api: NasaImagesApi) {
    suspend fun searchImages(query: String?, page: Int): List<SpaceImage> {
        val response = api.searchImages(
            query = query,
            page = page
        )

        return response.collection.items
            .mapNotNull { it.toDomainOrNull() }
    }
}