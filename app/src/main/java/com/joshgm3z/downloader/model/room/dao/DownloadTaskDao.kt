package com.joshgm3z.downloader.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.joshgm3z.downloader.model.room.data.DownloadTask
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadTaskDao {

    @Insert
    suspend fun add(downloadTask: DownloadTask)

    @Update
    suspend fun update(downloadTask: DownloadTask)

    @Query("SELECT * FROM DownloadTask")
    fun getAll(): Flow<List<DownloadTask>>

    @Query("DELETE FROM DownloadTask")
    suspend fun deleteAll()
}