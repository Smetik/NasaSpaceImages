package com.example.nasaspaceimages.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface SpaceImageDao{

    @Query("SELECT * FROM space_images")
    suspend fun getAll(): List<SpaceImageEntity>

    @Query("SELECT * FROM space_images WHERE id = :imageId LIMIT 1")
    suspend fun getById(imageId: Long): SpaceImageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: SpaceImageEntity)

    @Update
    suspend fun update(image: SpaceImageEntity)

    @Query("DELETE FROM space_images WHERE id = :imageId")
    suspend fun deleteById(imageId: Long)
}