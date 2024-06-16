package com.joshgm3z.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.model.DownloadRepository
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsListViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _downloadTasks = MutableStateFlow(DownloadTask.samples)
    val downloadTasks = _downloadTasks.asStateFlow()

    init {
        viewModelScope.launch {
            downloadRepository.getAllDownloads().collectLatest {
                Logger.entry()
                _downloadTasks.emit(it)
            }
        }
    }

    fun deleteDownload(downloadTask: DownloadTask) {
        viewModelScope.launch {
            downloadRepository.deleteDownload(downloadTask)
        }
    }

}

