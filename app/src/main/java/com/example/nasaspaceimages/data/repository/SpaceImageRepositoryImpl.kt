package com.example.nasaspaceimages.data.repository

import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository


class SpaceImageRepositoryImpl: SpaceImageRepository{
    private val savedImages = mutableListOf<SpaceImage>(
        SpaceImage(
            id = 1,
            nasaId = "EARTH001",
            title = "Earth",
            description = "Our planet",
            dateCreated = "2024-01-01",
            type = MediaType.IMAGE,
            mediaUrl = "https://example.com/earth.jpg",
            keywords = listOf("Earth", "Planet"),
            comment = "",
            isFavorite = false
        ),
        SpaceImage(
            id = 2,
            nasaId = "MARS002",
            title = "Mars",
            description = "Not our planet",
            dateCreated = "2025-02-01",
            type = MediaType.IMAGE,
            mediaUrl = "https://example.com/mars.jpg",
            keywords = listOf("Mars", "Planet"),
            comment = "",
            isFavorite = true
        )
    )


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

    private var nextId:Long = 3L

    override suspend fun getSavedImages(): List<SpaceImage> {
        return savedImages.toList()
    }

    override suspend fun getById(imageId: Long): SpaceImage? {
        return savedImages.find { it.id == imageId }
    }

    override suspend fun saveImage(image: SpaceImage) {
        savedImages.add(image.copy(id = nextId))
        nextId++
    }

    override suspend fun update(image: SpaceImage) {
        val index = savedImages.indexOfFirst { it.id == image.id }
        if (index != -1){
            savedImages[index] = image
        }
    }

    override suspend fun delete(imageId: Long) {
        savedImages.removeAll { it.id == imageId }
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