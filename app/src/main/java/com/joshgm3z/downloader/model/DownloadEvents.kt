package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.StateFlow

interface DownloadEvents {
    fun taskUpdates(): StateFlow<DownloadTask?>
}