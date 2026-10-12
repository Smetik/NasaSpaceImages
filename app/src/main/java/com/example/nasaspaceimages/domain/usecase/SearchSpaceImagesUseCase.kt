package com.example.nasaspaceimages.domain.usecase

import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import javax.inject.Inject

class SearchSpaceImagesUseCase @Inject constructor(private val repository: SpaceImageRepository){

    suspend operator fun invoke(query: String?, page: Int): List<SpaceImage>{
        val correctedPage = if (page < 1) 1 else page
        val spaceImages = repository.searchImages(query, correctedPage)
        return spaceImages
    }
}