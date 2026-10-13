package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.data.local.SpaceImageLocalDataSource
import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject


class SpaceImageRepositoryImpl @Inject constructor(
    private val localDataSource: SpaceImageLocalDataSource): SpaceImageRepository{
        private val remoteImages = listOf<SpaceImage>(
            SpaceImage(
                id = 0,
                nasaId = "Moon001",
                title = "Moon",
                description = "Not our planet",
                dateCreated = "2024-03-01",
                type = MediaType.IMAGE,
                mediaUrl = "https://example.com/moon.jpg",
                keywords = listOf("Moon", "Planet"),
                comment = "",
                isFavorite = false
            ),

            SpaceImage(
                id = 0,
                nasaId = "Apollo11001",
                title = "Apollo11",
                description = "Not our planet",
                dateCreated = "2024-04-01",
                type = MediaType.IMAGE,
                mediaUrl = "https://example.com/apollo11.jpg",
                keywords = listOf("Apollo11", "Planet"),
                comment = "",
                isFavorite = false
            ),

            SpaceImage(
                id = 0,
                nasaId = "Hubble001",
                title = "Hubble",
                description = "Not our planet",
                dateCreated = "2024-05-01",
                type = MediaType.IMAGE,
                mediaUrl = "https://example.com/hubble.jpg",
                keywords = listOf("Hubble", "Planet"),
                comment = "",
                isFavorite = false
            ),
        )


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
        ): List<SpaceImage> {

            val pageSize = 2
            val startIndex = (page - 1) * pageSize

            val filteredImages: List<SpaceImage> = if (query.isNullOrBlank()){
                remoteImages.toList()
            }
            else{
                remoteImages.filter { it.title.contains(query, ignoreCase = true) }
            }

            return filteredImages.drop(startIndex).take(pageSize)
        }


}