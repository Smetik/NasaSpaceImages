package com.example.nasaspaceimages.data.remote

import com.example.nasaspaceimages.data.remote.dto.NasaAssetResponseDto
import com.example.nasaspaceimages.data.remote.dto.NasaSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaImagesApi {

    @GET("search")
    suspend fun searchImages(
        @Query("q") query: String?,
        @Query("media_type") mediaType: String = "image",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("center") center: String? = null,
        @Query("year_start") yearStart: Int? = null,
        @Query("year_end") yearEnd: Int? = null
    ): NasaSearchResponseDto

    @GET("asset/{nasaId}")
    suspend fun getAssets(
        @Path("nasaId") nasaId: String
    ): NasaAssetResponseDto
}