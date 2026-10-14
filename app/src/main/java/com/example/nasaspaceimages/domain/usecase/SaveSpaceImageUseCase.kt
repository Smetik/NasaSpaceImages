package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class SaveSpaceImageUseCase @Inject constructor(
    private val repository: SpaceImageRepository
) {
    suspend operator fun invoke(image: SpaceImage) {
        repository.saveImage(image)
    }
}