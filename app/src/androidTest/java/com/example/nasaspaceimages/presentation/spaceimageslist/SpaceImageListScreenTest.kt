package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.nasaspaceimages.domain.model.MediaType
import com.example.nasaspaceimages.domain.model.SearchFilters
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.model.SpaceImageSearchPage
import com.example.nasaspaceimages.domain.repository.SpaceImageRepository
import com.example.nasaspaceimages.domain.usecase.DeleteSpaceImageUseCase
import com.example.nasaspaceimages.domain.usecase.SortSpaceImagesUseCase
import com.example.nasaspaceimages.domain.usecase.UpdateSpaceImageUseCase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SpaceImageListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun testImage() = SpaceImage(
        id = 1L,
        nasaId = "NASA-1",
        title = "Туманность Ориона",
        description = "Описание",
        dateCreated = "2025-01-01",
        type = MediaType.IMAGE,
        mediaUrl = "",
        keywords = emptyList(),
        comment = "",
        isFavorite = false
    )

    private fun setContent(
        images: List<SpaceImage> = listOf(testImage()),
        onOpen: (SpaceImage) -> Unit = {}
    ) {
        val repository = UiFakeSpaceImageRepository(images)

        val viewModel = SpaceImageListViewModel(
            sortSpaceImagesUseCase = SortSpaceImagesUseCase(repository),
            updateSpaceImageUseCase = UpdateSpaceImageUseCase(repository),
            deleteSpaceImageUseCase = DeleteSpaceImageUseCase(repository)
        )

        composeTestRule.setContent {
            MaterialTheme {
                SpaceImageListScreen(
                    onImageClick = onOpen,
                    listState = rememberLazyListState(),
                    viewModel = viewModel
                )
            }
        }
    }

    @Test
    fun imageTitleIsDisplayed() {
        setContent()

        composeTestRule
            .onNodeWithText("Туманность Ориона")
            .assertIsDisplayed()
    }

    @Test
    fun imageClickOpensDetails() {
        var openedId: Long? = null

        setContent(
            onOpen = { openedId = it.id }
        )

        composeTestRule
            .onNodeWithText("Туманность Ориона")
            .performClick()

        composeTestRule.runOnIdle {
            assertEquals(1L, openedId)
        }
    }

    @Test
    fun favoriteButtonUpdatesState() {
        setContent()

        composeTestRule
            .onNodeWithContentDescription("Добавить в избранное")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Убрать из избранного")
            .assertIsDisplayed()
    }

    @Test
    fun deleteRequiresConfirmation() {
        setContent()

        composeTestRule
            .onNodeWithContentDescription("Удалить")
            .performClick()

        composeTestRule
            .onNodeWithText("Удалить фотографию?")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Отмена")
            .performClick()

        composeTestRule
            .onNodeWithText("Туманность Ориона")
            .assertIsDisplayed()
    }

    @Test
    fun confirmedDeletionRemovesImage() {
        setContent()

        composeTestRule
            .onNodeWithContentDescription("Удалить")
            .performClick()

        composeTestRule
            .onNodeWithText("Удалить")
            .performClick()

        composeTestRule
            .onNodeWithText("Туманность Ориона")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Пока нет сохранённых изображений")
            .assertIsDisplayed()
    }
}

private class UiFakeSpaceImageRepository(
    initialImages: List<SpaceImage>
) : SpaceImageRepository {

    private val images = initialImages.toMutableList()

    override suspend fun getSavedImages(): List<SpaceImage> {
        return images.toList()
    }

    override suspend fun getById(imageId: Long): SpaceImage? {
        return images.find { it.id == imageId }
    }

    override suspend fun saveImage(image: SpaceImage) {
        images.add(image)
    }

    override suspend fun update(image: SpaceImage) {
        val index = images.indexOfFirst { it.id == image.id }

        if (index != -1) {
            images[index] = image
        }
    }

    override suspend fun delete(imageId: Long) {
        images.removeAll { it.id == imageId }
    }

    override suspend fun searchImages(
        query: String?,
        page: Int,
        filters: SearchFilters
    ): SpaceImageSearchPage {
        return SpaceImageSearchPage(
            images = emptyList(),
            hasNextPage = false
        )
    }

    override suspend fun downloadImage(image: SpaceImage): Long {
        return 1L
    }
}