package com.example.nasaspaceimages.data.di

import com.example.nasaspaceimages.data.repository.SpaceImageRepositoryImpl
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSpaceImageRepository(impl: SpaceImageRepositoryImpl): SpaceImageRepository
}