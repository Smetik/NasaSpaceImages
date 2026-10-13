package com.example.nasaspaceimages.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase


@Database(
    entities = [SpaceImageEntity::class],
    version = 1
)

abstract class SpaceImagesDatabase: RoomDatabase() {
    abstract fun spaceImageDao(): SpaceImageDao
}