package com.example.nasaspaceimages.data.di

import android.content.Context
import androidx.room3.Room
import com.example.nasaspaceimages.data.local.MIGRATION_1_2
import com.example.nasaspaceimages.data.local.SpaceImageDao
import com.example.nasaspaceimages.data.local.SpaceImagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SpaceImagesDatabase {
        return Room.databaseBuilder(
            context,
            SpaceImagesDatabase::class.java,
            "space_images.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideSpaceImageDao(
        database: SpaceImagesDatabase
    ): SpaceImageDao {
        return database.spaceImageDao()
    }
}