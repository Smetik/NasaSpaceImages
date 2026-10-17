package com.example.nasaspaceimages.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NasaAssetResponseDto(
    val collection: NasaAssetCollectionDto
)

@Serializable
data class NasaAssetCollectionDto(
    val items: List<NasaAssetItemDto> = emptyList()
)

@Serializable
data class NasaAssetItemDto(
    val href: String
)