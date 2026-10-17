package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class SaveSpaceImageUseCase @Inject constructor(
    private val repository: SpaceImageRepository
){
    suspend operator fun invoke(image: SpaceImage):Boolean {
        val savedImages = repository.getSavedImages()

        if (savedImages.any { it.nasaId == image.nasaId }) {
            return false
        }
        repository.saveImage(image)
        return true

    }
}