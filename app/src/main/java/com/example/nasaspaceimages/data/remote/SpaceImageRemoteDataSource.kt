package com.example.nasaspaceimages.data.remote

import com.example.nasaspaceimages.data.mapper.toDomainOrNull
import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import javax.inject.Inject

class SpaceImageRemoteDataSource @Inject constructor(private val api: NasaImagesApi) {
    suspend fun searchImages(
        query: String?,
        page: Int,
        filters: SearchFilters
    ): SpaceImageSearchPage {
        val response = api.searchImages(
            query = query,
            page = page,
            center = filters.center,
            yearStart = filters.yearStart,
            yearEnd = filters.yearEnd
        )

        return SpaceImageSearchPage(
            images = response.collection.items
                .mapNotNull { it.toDomainOrNull() },
            hasNextPage = response.collection.links.any {
                it.rel == "next"
            }
        )
    }

    suspend fun getOriginalImageUrl(nasaId: String): String? {
        val response = api.getAssets(nasaId)

        val imageRegex = Regex(
            """\.(jpg|jpeg|png|webp|tif|tiff)(\?.*)?$""",
            RegexOption.IGNORE_CASE
        )

        val urls = response.collection.items
            .map {
                it.href.replaceFirst(
                    "http://images-assets.nasa.gov/",
                    "https://images-assets.nasa.gov/",
                    ignoreCase = true
                )
            }
            .filter { imageRegex.containsMatchIn(it) }

        return urls.firstOrNull { it.contains("~orig", ignoreCase = true) }
            ?: urls.firstOrNull { it.contains("~large", ignoreCase = true) }
            ?: urls.firstOrNull { it.contains("~medium", ignoreCase = true) }
            ?: urls.firstOrNull()
    }
}