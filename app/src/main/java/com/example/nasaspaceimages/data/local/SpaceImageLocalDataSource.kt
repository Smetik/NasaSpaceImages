package com.example.nasaspaceimages.data.local

import com.example.nasaspaceimages.data.mapper.toDomain
import com.example.nasaspaceimages.data.mapper.toEntity
import com.example.nasaspaceimages.domain.model.SpaceImage
import javax.inject.Inject

class SpaceImageLocalDataSource @Inject constructor( private val dao: SpaceImageDao) {
    suspend fun getSavedImages(): List<SpaceImage> {
        return dao.getAll().map { it.toDomain() }
    }

    suspend fun getById(imageId: Long): SpaceImage? {
        return dao.getById(imageId)?.toDomain()
    }

    suspend fun saveImage(image: SpaceImage) {
        dao.insert(image.toEntity())
    }

    suspend fun update(image: SpaceImage) {
        dao.update(image.toEntity())
    }

    suspend fun delete(imageId: Long) {
        dao.deleteById(imageId)
    }
}