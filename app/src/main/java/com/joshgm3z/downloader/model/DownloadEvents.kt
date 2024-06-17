package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.StateFlow

interface DownloadEvents {
    fun addTaskToQueue(downloadTask: DownloadTask)
    fun isInQueue(downloadTask: DownloadTask): Boolean
    fun taskUpdates(): StateFlow<DownloadTask?>
}