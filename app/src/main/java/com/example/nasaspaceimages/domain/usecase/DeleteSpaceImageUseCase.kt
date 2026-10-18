package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.repository.SavedImagesRepository
import javax.inject.Inject

class DeleteSpaceImageUseCase @Inject constructor(
    private val repository: SavedImagesRepository
) {
    suspend operator fun invoke(imageId: Long) {
        repository.delete(imageId)
    }
}