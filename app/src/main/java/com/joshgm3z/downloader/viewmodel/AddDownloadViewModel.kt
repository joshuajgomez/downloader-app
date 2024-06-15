package com.joshgm3z.downloader.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.DownloadRepository
import com.joshgm3z.downloader.model.room.data.DownloadTask
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
    data class Close(val message: String = "") : AddUiState()
}

@HiltViewModel
class AddDownloadViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<AddUiState> =
        MutableStateFlow(AddUiState.Init())
    val uiState: StateFlow<AddUiState> = _uiState

    var downloadTask: DownloadTask? = null

    @Inject
    lateinit var downloadRepository: DownloadRepository

    fun onDownloadClick() {
        viewModelScope.launch {
            downloadTask?.let {
                downloadRepository.addDownload(it) {
                    _uiState.value = AddUiState.Added()
                    Handler(Looper.getMainLooper()).postDelayed({
                        _uiState.value = AddUiState.Close()
                    }, 1000)
                }
            }
        }
    }

    fun onUrlEntered(url: String) {
        if (url.isEmpty()) {
            _uiState.value = AddUiState.Init()
            return
        }
        _uiState.value = AddUiState.Fetching()
        viewModelScope.launch {
            downloadRepository.checkUrl(url,
                onFileFound = {
                    downloadTask = it
                    _uiState.value = AddUiState.Metadata(it)
                },
                onError = { _uiState.value = AddUiState.Error(it) }
            )
        }
    }
}