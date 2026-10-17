package com.example.nasaspaceimages.domain.model

data class SpaceImageSearchPage(
    val images: List<SpaceImage>,
    val hasNextPage: Boolean
)