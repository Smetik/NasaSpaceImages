package com.example.nasaspaceimages.domain.model

data class SpaceImage (
    val id: Long,
    val nasaId: String,
    val title: String,
    val description: String,
    val dateCreated: String,
    val center: String = "",
    val type: MediaType,
    val mediaUrl: String,
    val keywords: List<String>,
    val comment: String,
    val isFavorite: Boolean
)