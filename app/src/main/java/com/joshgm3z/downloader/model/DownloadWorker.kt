package com.joshgm3z.downloader.model

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DownloadManagerModule {
    @Provides
    fun providesDownloadManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}

@Singleton
class DownloadWorker @Inject constructor(
    private val downloadManager: DownloadManager
) {

    private var isDownloadFinished = false

    private var downloadId: Long = 0

    private val _downloadTaskFlow = MutableStateFlow<DownloadTask?>(null)
    val downloadTaskFlow: StateFlow<DownloadTask?> = _downloadTaskFlow

    fun download(
        downloadTask: DownloadTask,
    ) {
        _downloadTaskFlow.value = downloadTask
        Logger.debug("downloadTask = [${downloadTask}]")
        val uri = Uri.parse(downloadTask.url)
        DownloadManager.Request(uri).apply {
            setTitle(downloadTask.filename)
            setMimeType(downloadTask.mime)
            setDescription("Downloading ${downloadTask.filename}")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                downloadTask.filename
            )
            downloadId = downloadManager.enqueue(this)
            isDownloadFinished = false
        }
        checkStatus()
    }


    @SuppressLint("Range")
    private fun checkStatus() {
        while (!isDownloadFinished) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToFirst()) {
                val downloadStatus = getDownloadStatus(cursor)
                val progress: Long = getProgress(cursor)
                val currentSize = getDownloadedBytes(cursor)
                notifyProgress(DownloadState.RUNNING, progress, currentSize)

                when (downloadStatus) {
                    DownloadManager.STATUS_RUNNING -> {
                        isDownloadFinished = false
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        isDownloadFinished = true
                        notifyProgress(DownloadState.COMPLETED, progress, currentSize)
                    }

                    DownloadManager.STATUS_FAILED -> {
                        isDownloadFinished = true
                        notifyProgress(DownloadState.FAILED, progress, currentSize)
                    }
                }
            }
            cursor.close()
        }
    }

    private fun getProgress(cursor: Cursor): Long {
        var progress: Long = 0
        val totalBytes = getTotalBytes(cursor)
        val downloadedBytes = getDownloadedBytes(cursor)
        if (totalBytes > 0) {
            progress = downloadedBytes * 100 / totalBytes
        }
        Logger.info(
            "progress=$progress," +
                    " totalBytes=$totalBytes," +
                    " downloadedBytes=$downloadedBytes," +
                    " status=${getDownloadStatus(cursor)}"
        )
        return progress
    }

    @SuppressLint("Range")
    private fun getDownloadedBytes(cursor: Cursor): Long {
        return cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
    }

    @SuppressLint("Range")
    private fun getTotalBytes(cursor: Cursor): Long {
        return cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
    }

    @SuppressLint("Range")
    private fun getDownloadStatus(cursor: Cursor): Int {
        return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
    }

    private fun notifyProgress(state: DownloadState, progress: Long, currentSize: Long) {
//        Logger.debug("state = [${state}], progress = [${progress}]")
        _downloadTaskFlow.update {
            it?.copy(
                progress = progress,
                state = state,
                currentSize = currentSize
            )
        }
    }

}
