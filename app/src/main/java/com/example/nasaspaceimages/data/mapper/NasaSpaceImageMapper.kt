package com.example.nasaspaceimages.data.mapper

import com.example.nasaspaceimages.data.remote.dto.NasaItemDto
import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SpaceImage

fun NasaItemDto.toDomainOrNull(): SpaceImage? {
    val data = data.firstOrNull() ?: return null

    val mediaUrl = links
        .firstOrNull { it.rel == "preview" }
        ?.href
        ?: return null

    val mediaType = when (data.mediaType.lowercase()) {
        "image" -> MediaType.IMAGE
        "video" -> MediaType.VIDEO
        "audio" -> MediaType.AUDIO
        else -> return null
    }

    return SpaceImage(
        id = 0,
        nasaId = data.nasaId,
        title = data.title,
        description = data.description,
        dateCreated = data.dateCreated,
        center = data.center,
        type = mediaType,
        mediaUrl = mediaUrl,
        keywords = data.keywords,
        comment = "",
        isFavorite = false
    )
}