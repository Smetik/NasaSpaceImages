package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.data.local.SpaceImageLocalDataSource
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SavedImagesRepository
import javax.inject.Inject

class SavedImagesRepositoryImpl @Inject constructor(
    private val localDataSource: SpaceImageLocalDataSource
) : SavedImagesRepository {

    override suspend fun getSavedImages(): List<SpaceImage> {
        return localDataSource.getSavedImages()
    }

    override suspend fun getById(imageId: Long): SpaceImage? {
        return localDataSource.getById(imageId)
    }

    override suspend fun saveImage(image: SpaceImage) {
        localDataSource.saveImage(image)
    }

    override suspend fun update(image: SpaceImage) {
        localDataSource.update(image)
    }

    override suspend fun delete(imageId: Long) {
        localDataSource.delete(imageId)
    }
}