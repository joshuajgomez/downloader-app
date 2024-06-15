package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadRepository @Inject constructor(
    private val downloadTaskDao: DownloadTaskDao
) {
    suspend fun addDownload(downloadTask: DownloadTask, onAdded: () -> Unit) {
        downloadTaskDao.add(downloadTask)
        onAdded()
    }

    suspend fun getAllDownloads(): Flow<List<DownloadTask>> {
        return downloadTaskDao.getAll()
    }

    fun checkUrl(
        url: String,
        onFileFound: (downloadTask: DownloadTask) -> Unit,
        onError: (message: String) -> Unit
    ) {
        if (url.isNotEmpty()) {
            onFileFound(DownloadTask.new(url))
        } else {
            onError("Invalid url")
        }
    }

}
