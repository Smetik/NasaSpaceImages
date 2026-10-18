package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.ImageDownloadRepository
import javax.inject.Inject

class DownloadSpaceImageUseCase @Inject constructor(
    private val repository: ImageDownloadRepository
) {
    suspend operator fun invoke(image: SpaceImage): Long {
        return repository.downloadImage(image)
    }
}