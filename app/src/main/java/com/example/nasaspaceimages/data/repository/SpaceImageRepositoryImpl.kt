package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.data.local.SpaceImageLocalDataSource
import com.example.nasaspaceimages.data.remote.SpaceImageRemoteDataSource
import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject


class SpaceImageRepositoryImpl @Inject constructor(
    private val localDataSource: SpaceImageLocalDataSource,
    private val remoteDataSource: SpaceImageRemoteDataSource
): SpaceImageRepository{

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

    override suspend fun searchImages(
        query: String?,
        page: Int
    ): SpaceImageSearchPage {
        return remoteDataSource.searchImages(query, page)
    }

}