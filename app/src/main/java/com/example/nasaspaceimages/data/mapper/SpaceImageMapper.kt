package com.example.nasaspaceimages.data.mapper

import com.example.nasaspaceimages.data.local.SpaceImageEntity
import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SpaceImage

fun SpaceImage.toEntity(): SpaceImageEntity {
    return SpaceImageEntity(
        id = id,
        nasaId = nasaId,
        title = title,
        description = description,
        dateCreated = dateCreated,
        center = center,
        type = type.name,
        mediaUrl = mediaUrl,
        keywords = keywords.joinToString("|"),
        comment = comment,
        isFavorite = isFavorite
    )
}

fun SpaceImageEntity.toDomain(): SpaceImage {
    return SpaceImage(
        id = id,
        nasaId = nasaId,
        title = title,
        description = description,
        dateCreated = dateCreated,
        center = center,
        type = MediaType.valueOf(type),
        mediaUrl = mediaUrl,
        keywords = if (keywords.isBlank()) {
            emptyList()
        } else {
            keywords.split("|")
        },
        comment = comment,
        isFavorite = isFavorite
    )
}