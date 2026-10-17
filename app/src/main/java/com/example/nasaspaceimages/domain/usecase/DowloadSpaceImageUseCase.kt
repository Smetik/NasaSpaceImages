package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class DownloadSpaceImageUseCase @Inject constructor(
    private val repository: SpaceImageRepository
) {
    suspend operator fun invoke(image: SpaceImage): Long {
        return repository.downloadImage(image)
    }
}