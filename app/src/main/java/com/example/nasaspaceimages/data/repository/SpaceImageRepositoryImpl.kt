package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.data.local.AndroidImageDownloader
import com.example.nasaspaceimages.data.local.SpaceImageLocalDataSource
import com.example.nasaspaceimages.data.remote.SpaceImageRemoteDataSource
import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject


class SpaceImageRepositoryImpl @Inject constructor(
    private val localDataSource: SpaceImageLocalDataSource,
    private val remoteDataSource: SpaceImageRemoteDataSource,
    private val imageDownloader: AndroidImageDownloader
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
        page: Int,
        filters: SearchFilters
    ): SpaceImageSearchPage {
        return remoteDataSource.searchImages(
            query = query,
            page = page,
            filters = filters
        )
    }

    override suspend fun downloadImage(image: SpaceImage): Long {
        val url = remoteDataSource.getOriginalImageUrl(image.nasaId)
            ?: image.mediaUrl

        return imageDownloader.download(
            url = url,
            nasaId = image.nasaId,
            title = image.title
        )
    }
}