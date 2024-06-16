package com.joshgm3z.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.DownloadManager
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.model.DownloadRepository
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsListViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository,
    private val downloadManager: DownloadManager,
) : ViewModel() {

    private val _downloadTasks = MutableStateFlow(DownloadTask.samples)
    val downloadTasks = _downloadTasks.asStateFlow()

    init {
        viewModelScope.launch {
            listenListUpdates()
        }
        viewModelScope.launch {
            listenStateUpdates()
        }
    }

    private suspend fun listenListUpdates() {
        Logger.entry()
        downloadRepository.getAllDownloads().collectLatest {
            Logger.debug("it= $it")
            _downloadTasks.value = it
        }
    }

    private suspend fun listenStateUpdates() {
        Logger.entry()
        downloadManager.downloadTaskFlow.collectLatest { update ->
            Logger.debug("update = [${update}]")
            if (update == null) return@collectLatest
            _downloadTasks.update { downloadTasks ->
                var count = 0
                for (downloadTask in downloadTasks) {
                    count++
                    if (downloadTask.id == update.id) {
                        break
                    }
                }
                downloadTasks.toMutableList().apply {
                    if (count < size) {
                        removeAt(count)
                        add(count, update)
                    }
                }
            }
        }
    }

    fun deleteDownload(downloadTask: DownloadTask) {
        viewModelScope.launch {
            downloadRepository.deleteDownload(downloadTask)
        }
    }

}

