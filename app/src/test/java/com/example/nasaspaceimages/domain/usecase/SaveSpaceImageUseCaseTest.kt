package com.example.nasaspaceimages.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class SaveSpaceImageUseCaseTest {

    @Test
    fun `новая фотография успешно сохраняется`() = runTest {
        val repository = FakeSpaceImageRepository()
        val useCase = SaveSpaceImageUseCase(repository)
        val image = testSpaceImage()

        val result = useCase(image)

        assertTrue(result)
        assertEquals(listOf(image), repository.getSavedImages())
    }

    @Test
    fun `повторное сохранение запрещено`() = runTest {
        val repository = FakeSpaceImageRepository()
        val useCase = SaveSpaceImageUseCase(repository)
        val image = testSpaceImage()

        useCase(image)
        val result = useCase(image)

        assertFalse(result)
        assertEquals(1, repository.getSavedImages().size)
    }

    @Test
    fun `разные NASA ID сохраняются отдельно`() = runTest {
        val repository = FakeSpaceImageRepository()
        val useCase = SaveSpaceImageUseCase(repository)

        val first = testSpaceImage(
            id = 1L,
            nasaId = "NASA-1"
        )

        val second = testSpaceImage(
            id = 2L,
            nasaId = "NASA-2"
        )

        val firstResult = useCase(first)
        val secondResult = useCase(second)

        assertTrue(firstResult)
        assertTrue(secondResult)
        assertEquals(2, repository.getSavedImages().size)
    }

    @Test
    fun `дубликат определяется по NASA ID а не локальному ID`() = runTest {
        val repository = FakeSpaceImageRepository()
        val useCase = SaveSpaceImageUseCase(repository)

        val first = testSpaceImage(
            id = 1L,
            nasaId = "NASA-123"
        )

        val second = testSpaceImage(
            id = 99L,
            nasaId = "NASA-123"
        )

        useCase(first)
        val result = useCase(second)

        assertFalse(result)
        assertEquals(1, repository.getSavedImages().size)
    }
}