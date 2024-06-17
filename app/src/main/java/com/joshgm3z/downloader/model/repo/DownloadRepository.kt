package com.joshgm3z.downloader.model.repo

import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val downloadTaskDao: DownloadTaskDao,
) {
    suspend fun addDownload(downloadTask: DownloadTask) {
        downloadTaskDao.add(downloadTask)
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

}
