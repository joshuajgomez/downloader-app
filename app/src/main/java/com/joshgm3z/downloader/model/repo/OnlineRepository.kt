package com.joshgm3z.downloader.model.repo;

import android.os.Handler
import android.os.Looper
import com.joshgm3z.downloader.model.retrofit.DownloadService
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.FileManager
import com.joshgm3z.downloader.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnlineRepository @Inject constructor(
    private val fileManager: FileManager,
    private val scope: CoroutineScope,
    private val downloadService: DownloadService,
) {
    fun checkUrl(
        url: String,
        onFileFound: (downloadTask: DownloadTask) -> Unit,
        onError: (message: String) -> Unit
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (url.isNotEmpty()) {
                getFileSize(url) {
                    val new = DownloadTask.new(url)
                    new.totalSize = it
                    new.mime = fileManager.getMimeType(url)

                    onFileFound(new)
                }
            } else {
                onError("Invalid url")
            }
        }, 1000)
    }

    private fun getFileSize(url: String, onFileSize: (size: Long) -> Unit) {
        scope.launch {
            val fileSize = downloadService.downloadFile(url).bytes().size
            Logger.info("size=$fileSize, url=$url")
            onFileSize(fileSize.toLong())
        }
    }

}
