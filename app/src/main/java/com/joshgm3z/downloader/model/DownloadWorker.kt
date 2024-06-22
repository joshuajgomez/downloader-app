package com.joshgm3z.downloader.model

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import com.joshgm3z.downloader.model.retrofit.DownloadService
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
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
    private val scope: CoroutineScope,
    private val downloadService: DownloadService
) {

    private val _downloadTaskFlow = MutableStateFlow<DownloadTask?>(null)
    val downloadTaskFlow: StateFlow<DownloadTask?> = _downloadTaskFlow

    fun download(
        downloadTask: DownloadTask,
    ) {
        Logger.debug("downloadTask = [${downloadTask}]")
        _downloadTaskFlow.value = downloadTask
        scope.launch {
            downloadService
                .downloadFile(downloadTask.url)
                .saveFile(getFilePath(downloadTask))
        }
    }

    private fun getFilePath(downloadTask: DownloadTask): String {
        val downloads = Environment
            .getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            .path
        return "$downloads/${downloadTask.filename}"
    }

    private fun notifyProgress(state: DownloadState, currentSize: Long) {
        Logger.debug("state = [${state}], currentSize = [${currentSize}]")
        _downloadTaskFlow.update {
            val progress = ((currentSize * 100) / it!!.totalSize)
            it.copy(
                progress = progress,
                state = state,
                currentSize = currentSize
            )
        }
    }

    private fun ResponseBody.saveFile(destinationPath: String) {
        val file = File(destinationPath)
        var progressBytes: Long = 0
        notifyProgress(DownloadState.RUNNING, progressBytes)
        try {
            byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        outputStream.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = inputStream.read(buffer)
                        notifyProgress(DownloadState.RUNNING, progressBytes)
                    }
                }
            }
            notifyCompletion(progressBytes, destinationPath)
        } catch (e: Exception) {
            Logger.error(e.message ?: "Download failed with exception")
            notifyProgress(DownloadState.FAILED, progressBytes)
        }
    }

    private fun notifyCompletion(totalBytes: Long, path: String) {
        Logger.debug("totalBytes = [${totalBytes}]")
        _downloadTaskFlow.update {
            it?.copy(
                state = DownloadState.COMPLETED,
                totalSize = totalBytes,
                localPath = path
            )
        }
    }
}