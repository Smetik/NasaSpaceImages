package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.data.local.AndroidImageDownloader
import com.example.nasaspaceimages.data.remote.SpaceImageRemoteDataSource
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.ImageDownloadRepository
import javax.inject.Inject

class ImageDownloadRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpaceImageRemoteDataSource,
    private val imageDownloader: AndroidImageDownloader
) : ImageDownloadRepository {

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