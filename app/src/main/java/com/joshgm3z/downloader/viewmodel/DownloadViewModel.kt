package com.joshgm3z.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.model.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _downloadTasks: MutableStateFlow<List<DownloadTask>> = MutableStateFlow(emptyList())
    val downloadTasks: StateFlow<List<DownloadTask>> = _downloadTasks

    init {
        viewModelScope.launch {
            downloadRepository.getAllDownloads().collectLatest {
                _downloadTasks.value = it
            }
        }
    }

}

