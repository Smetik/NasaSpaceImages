package com.example.nasaspaceimages.presentation.spaceimageslist

import com.example.nasaspaceimages.domain.model.SortOption
import com.example.nasaspaceimages.domain.usecase.DeleteSpaceImageUseCase
import com.example.nasaspaceimages.domain.usecase.FakeSpaceImageRepository
import com.example.nasaspaceimages.domain.usecase.SortSpaceImagesUseCase
import com.example.nasaspaceimages.domain.usecase.UpdateSpaceImageUseCase
import com.example.nasaspaceimages.domain.usecase.testSpaceImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class SpaceImageListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        repository: FakeSpaceImageRepository
    ): SpaceImageListViewModel {
        return SpaceImageListViewModel(
            sortSpaceImagesUseCase = SortSpaceImagesUseCase(repository),
            updateSpaceImageUseCase = UpdateSpaceImageUseCase(repository),
            deleteSpaceImageUseCase = DeleteSpaceImageUseCase(repository)
        )
    }

    @Test
    fun `после загрузки фотографии отображаются`() = runTest {
        val repository = FakeSpaceImageRepository()
        repository.setImages(
            listOf(testSpaceImage())
        )

        val viewModel = createViewModel(repository)

        viewModel.refreshImages()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.images.size)
        assertEquals("Туманность Ориона", state.images.first().title)
    }

    @Test
    fun `изменение сортировки меняет порядок фотографий`() = runTest {
        val repository = FakeSpaceImageRepository()

        repository.setImages(
            listOf(
                testSpaceImage(1L, "NASA-1", "Вега"),
                testSpaceImage(2L, "NASA-2", "Андромеда")
            )
        )

        val viewModel = createViewModel(repository)

        viewModel.changeSortOption(SortOption.TITLE_ASC)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(SortOption.TITLE_ASC, state.sortOption)
        assertEquals(
            listOf("Андромеда", "Вега"),
            state.images.map { it.title }
        )
    }

    @Test
    fun `добавление в избранное меняет состояние фотографии`() = runTest {
        val repository = FakeSpaceImageRepository()
        val image = testSpaceImage(isFavorite = false)

        repository.setImages(listOf(image))

        val viewModel = createViewModel(repository)

        viewModel.toggleFavorite(image)
        advanceUntilIdle()

        val result = viewModel.uiState.value.images.first()

        assertTrue(result.isFavorite)
    }

    @Test
    fun `редактирование обновляет название и комментарий`() = runTest {
        val repository = FakeSpaceImageRepository()
        val image = testSpaceImage()

        repository.setImages(listOf(image))

        val viewModel = createViewModel(repository)

        viewModel.editImage(
            image = image,
            title = "  Новое название  ",
            comment = "  Изучено  "
        )
        advanceUntilIdle()

        val result = viewModel.uiState.value.images.first()

        assertEquals("Новое название", result.title)
        assertEquals("Изучено", result.comment)
    }

    @Test
    fun `удаление убирает фотографию из каталога`() = runTest {
        val repository = FakeSpaceImageRepository()
        val image = testSpaceImage()

        repository.setImages(listOf(image))

        val viewModel = createViewModel(repository)

        viewModel.deleteImage(image.id)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.images.isEmpty())
        assertTrue(repository.getSavedImages().isEmpty())
    }

    @Test
    fun `пустое название не сохраняется`() = runTest {
        val repository = FakeSpaceImageRepository()
        val image = testSpaceImage()

        repository.setImages(listOf(image))

        val viewModel = createViewModel(repository)

        viewModel.editImage(
            image = image,
            title = "   ",
            comment = "Новый комментарий"
        )
        advanceUntilIdle()

        val result = repository.getSavedImages().first()

        assertEquals("Туманность Ориона", result.title)
        assertEquals("", result.comment)
    }
}