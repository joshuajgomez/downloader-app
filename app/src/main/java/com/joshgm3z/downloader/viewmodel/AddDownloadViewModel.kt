package com.joshgm3z.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.repo.DownloadRepository
import com.joshgm3z.downloader.model.repo.OnlineRepository
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddUiState {
    data class Init(val message: String = "") : AddUiState()
    data class Fetching(val message: String = "Checking URL") : AddUiState()
    data class Metadata(val downloadTask: DownloadTask) : AddUiState()
    data class Added(val message: String = "Download added") : AddUiState()
    data class Error(val message: String = "Invalid URL") : AddUiState()
}

@HiltViewModel
class AddDownloadViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<AddUiState> =
        MutableStateFlow(AddUiState.Init())
    val uiState: StateFlow<AddUiState> = _uiState

    private var downloadTask: DownloadTask? = null

    @Inject
    lateinit var downloadRepository: DownloadRepository

    @Inject
    lateinit var onlineRepository: OnlineRepository

    fun onDownloadClick() {
        viewModelScope.launch {
            downloadTask?.let {
                downloadRepository.addDownload(it)
                _uiState.value = AddUiState.Added()
            } ?: Logger.warn("downloadTask is null")
        }
    }

    fun onUrlEntered(url: String) {
        if (url.isEmpty()) {
            _uiState.value = AddUiState.Init()
            return
        }
        _uiState.value = AddUiState.Fetching()
        viewModelScope.launch {
            onlineRepository.checkUrl(url,
                onFileFound = {
                    downloadTask = it
                    _uiState.value = AddUiState.Metadata(it)
                },
                onError = { _uiState.value = AddUiState.Error(it) }
            )
        }
    }

    fun resetUiState() {
        _uiState.value = AddUiState.Init()
    }
}