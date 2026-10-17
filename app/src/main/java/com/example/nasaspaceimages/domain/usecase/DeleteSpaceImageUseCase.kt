package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class DeleteSpaceImageUseCase @Inject constructor(
    private val repository: SpaceImageRepository
) {
    suspend operator fun invoke (imageId: Long){
        repository.delete(imageId)
    }
}