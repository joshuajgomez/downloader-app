package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val downloadTaskDao: DownloadTaskDao,
) {
    suspend fun addDownload(downloadTask: DownloadTask, onAdded: () -> Unit) {
        downloadTaskDao.add(downloadTask)
        onAdded()
    }

    fun getAllDownloads(): Flow<List<DownloadTask>> {
        return downloadTaskDao.getAll()
    }

    suspend fun deleteDownload(downloadTask: DownloadTask) {
        TODO("Not yet implemented")
    }

    suspend fun deleteAll() {
        downloadTaskDao.deleteAll()
    }

    suspend fun updateTaskState(downloadTask: DownloadTask) {
        downloadTaskDao.update(downloadTask)
    }

}
