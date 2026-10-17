package com.example.nasaspaceimages.domain.repository

import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage

interface SpaceImageRepository {
    suspend fun getSavedImages(): List<SpaceImage>
    suspend fun getById(imageId: Long): SpaceImage?
    suspend fun saveImage(image: SpaceImage)
    suspend fun update(image: SpaceImage)
    suspend fun delete(imageId: Long)

    suspend fun searchImages(query: String?, page: Int, filters: SearchFilters): SpaceImageSearchPage
}