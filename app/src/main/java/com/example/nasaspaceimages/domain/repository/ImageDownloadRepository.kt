package com.example.nasaspaceimages.domain.repository

import com.example.nasaspaceimages.domain.model.SpaceImage

interface ImageDownloadRepository {
    suspend fun downloadImage(image: SpaceImage): Long
}