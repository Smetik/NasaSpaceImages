package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SortOption
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class SortSpaceImagesUseCase @Inject constructor (private val repository: SpaceImageRepository) {
    suspend operator fun invoke (sortBy: SortOption): List<SpaceImage> {
        val spaceImages = repository.getSavedImages()

        spaceImages.forEach {
            println("${it.title} — ${it.dateCreated}")
        }

        return when (sortBy){
            SortOption.TITLE_ASC -> spaceImages.sortedBy { it.title }
            SortOption.TITLE_DESC -> spaceImages.sortedByDescending { it.title }
            SortOption.DATE_CREATED_NEWEST -> spaceImages.sortedByDescending { it.id }
            SortOption.DATE_CREATED_OLDEST -> spaceImages.sortedBy{ it.id }
            SortOption.FAVORITE_FIRST -> spaceImages.sortedByDescending { it.isFavorite }
            SortOption.FAVORITE_LAST -> spaceImages.sortedBy{ it.isFavorite }
        }
    }
}