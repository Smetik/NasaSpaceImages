package com.example.nasaspaceimages.domain.repository

import com.example.nasaspaceimages.domain.model.SpaceImage

interface SavedImagesRepository {
    suspend fun getSavedImages(): List<SpaceImage>
    suspend fun getById(imageId: Long): SpaceImage?
    suspend fun saveImage(image: SpaceImage)
    suspend fun update(image: SpaceImage)
    suspend fun delete(imageId: Long)
}