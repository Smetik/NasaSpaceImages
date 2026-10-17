package com.example.nasaspaceimages.data.local

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "space_images")
data class SpaceImageEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nasaId: String,
    val title: String,
    val description: String,
    val dateCreated: String,

    @ColumnInfo(defaultValue = "''")
    val center: String = "",

    val type: String,
    val mediaUrl: String,
    val keywords: String,
    val comment: String,
    val isFavorite: Boolean

)