package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SavedImagesRepository

class FakeSpaceImageRepository : SavedImagesRepository {

    private val images = mutableListOf<SpaceImage>()

    fun setImages(value: List<SpaceImage>) {
        images.clear()
        images.addAll(value)
    }

    override suspend fun getSavedImages(): List<SpaceImage> {
        return images.toList()
    }

    override suspend fun getById(imageId: Long): SpaceImage? {
        return images.find { it.id == imageId }
    }

    override suspend fun saveImage(image: SpaceImage) {
        images.add(image)
    }

    override suspend fun update(image: SpaceImage) {
        val index = images.indexOfFirst { it.id == image.id }

        if (index != -1) {
            images[index] = image
        }
    }

    override suspend fun delete(imageId: Long) {
        images.removeAll { it.id == imageId }
    }
}

fun testSpaceImage(
    id: Long = 1L,
    nasaId: String = "NASA-1",
    title: String = "Туманность Ориона",
    isFavorite: Boolean = false
): SpaceImage {
    return SpaceImage(
        id = id,
        nasaId = nasaId,
        title = title,
        description = "Космическое изображение",
        dateCreated = "2025-01-01",
        type = MediaType.IMAGE,
        mediaUrl = "",
        keywords = emptyList(),
        comment = "",
        isFavorite = isFavorite
    )
}