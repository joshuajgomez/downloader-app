package com.joshgm3z.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.DownloadEvents
import com.joshgm3z.downloader.model.repo.DownloadRepository
import com.joshgm3z.downloader.model.room.data.DownloadTask
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
    private val downloadEvents: DownloadEvents,
) : ViewModel() {

    private val _downloadTasks = MutableStateFlow(DownloadTask.samples)
    val downloadTasks = _downloadTasks.asStateFlow()

    init {
        Logger.entry()
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
        downloadEvents.taskUpdates().collectLatest { update ->
            Logger.debug("update = [${update}]")
            if (update == null) return@collectLatest
            _downloadTasks.update { downloadTasks ->
                var count = 0
                for (downloadTask in downloadTasks) {
                    if (downloadTask.id == update.id) {
                        break
                    }
                    count++
                }
                if (count == downloadTasks.size)
                    return@collectLatest

                downloadTasks.toMutableList().apply {
                    set(count, update)
                    Logger.info("updated: $this")
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

