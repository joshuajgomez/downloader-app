package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.RoomDb
import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadRepository @Inject constructor(
    private val downloadTaskDao: DownloadTaskDao
) {
    suspend fun addDownload(url: String) {
        downloadTaskDao.add(DownloadTask.new(url))
    }

    suspend fun getAllDownloads(): Flow<List<DownloadTask>> {
        return downloadTaskDao.getAll()
    }

}
