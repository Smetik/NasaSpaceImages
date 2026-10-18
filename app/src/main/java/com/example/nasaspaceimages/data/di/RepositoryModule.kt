package com.example.nasaspaceimages.data.di

import com.example.nasaspaceimages.data.repository.ImageDownloadRepositoryImpl
import com.example.nasaspaceimages.data.repository.NasaSearchRepositoryImpl
import com.example.nasaspaceimages.data.repository.SavedImagesRepositoryImpl
import com.example.nasaspaceimages.domain.repository.ImageDownloadRepository
import com.example.nasaspaceimages.domain.repository.NasaSearchRepository
import com.example.nasaspaceimages.domain.repository.SavedImagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSavedImagesRepository(
        impl: SavedImagesRepositoryImpl
    ): SavedImagesRepository

    @Binds
    @Singleton
    abstract fun bindNasaSearchRepository(
        impl: NasaSearchRepositoryImpl
    ): NasaSearchRepository

    @Binds
    @Singleton
    abstract fun bindImageDownloadRepository(
        impl: ImageDownloadRepositoryImpl
    ): ImageDownloadRepository
}