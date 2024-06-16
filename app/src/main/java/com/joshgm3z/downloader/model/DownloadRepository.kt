package com.joshgm3z.downloader.model

import android.os.Handler
import android.os.Looper
import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val downloadTaskDao: DownloadTaskDao
) {
    suspend fun addDownload(downloadTask: DownloadTask, onAdded: () -> Unit) {
        downloadTaskDao.add(downloadTask)
        onAdded()
    }

    fun getAllDownloads(): Flow<List<DownloadTask>> {
        return downloadTaskDao.getAll()
    }

    fun checkUrl(
        url: String,
        onFileFound: (downloadTask: DownloadTask) -> Unit,
        onError: (message: String) -> Unit
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (url.isNotEmpty()) {
                onFileFound(DownloadTask.new(url))
            } else {
                onError("Invalid url")
            }
        }, 1000)
    }

}
