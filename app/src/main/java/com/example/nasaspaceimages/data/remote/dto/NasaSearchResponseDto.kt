package com.example.nasaspaceimages.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NasaSearchResponseDto(
    val collection: NasaCollectionDto
)

@Serializable
data class NasaCollectionDto(
    val items: List<NasaItemDto>
)

@Serializable
data class NasaItemDto(
    val data: List<SpaceImageDto>,
    val links: List<NasaLinkDto> = emptyList()
)

@Serializable
data class NasaLinkDto(
    val href: String,
    val rel: String? = null,
    val render: String? = null
)