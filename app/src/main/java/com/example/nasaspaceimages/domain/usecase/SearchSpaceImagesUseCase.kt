package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class SearchSpaceImagesUseCase @Inject constructor(private val repository: SpaceImageRepository){

    suspend operator fun invoke(
        query: String?,
        page: Int
    ): SpaceImageSearchPage {
        val correctedPage = page.coerceAtLeast(1)

        return repository.searchImages(query, correctedPage)
    }
}