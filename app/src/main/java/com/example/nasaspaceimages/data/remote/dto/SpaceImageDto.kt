package com.example.nasaspaceimages.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SpaceImageDto (
    val center: String = "",

    @SerialName("data_created")
    val dateCreated: String = "",

    val description: String,
    val keywords: List<String> = emptyList(),

    @SerialName("media_type")
    val mediaType : String,

    @SerialName("nasa_id")
    val nasaId: String,

    val title: String
)