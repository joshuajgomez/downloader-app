package com.joshgm3z.downloader.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadTaskDao {
    @Query("SELECT * FROM DownloadTask")
    fun getAll(): Flow<List<DownloadTask>>

    @Insert
    suspend fun add(downloadTask: DownloadTask)
}