package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SavedImagesRepository
import javax.inject.Inject

class UpdateSpaceImageUseCase @Inject constructor(
    private val repository: SavedImagesRepository
) {
    suspend operator fun invoke(image: SpaceImage) {
        repository.update(image)
    }
}