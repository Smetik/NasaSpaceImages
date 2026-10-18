package com.example.nasaspaceimages.presentation.spaceimagedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaspaceimages.R
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.domain.usecase.DownloadSpaceImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpaceImageDetailViewModel @Inject constructor(
    private val downloadUseCase: DownloadSpaceImageUseCase
) : ViewModel() {

    private val _messages = Channel<Int>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    private var isDownloading = false

    fun downloadImage(image: SpaceImage) {
        if (isDownloading) return

        isDownloading = true

        viewModelScope.launch {
            try {
                downloadUseCase(image)
                _messages.send(R.string.download_started)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _messages.send(R.string.download_failed)
            } finally {
                isDownloading = false
            }
        }
    }
}