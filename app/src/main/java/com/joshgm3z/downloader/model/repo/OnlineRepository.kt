package com.joshgm3z.downloader.model.repo;

import android.os.Handler
import android.os.Looper
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.FileManager
import javax.inject.Inject

class OnlineRepository @Inject constructor(
    private val fileManager: FileManager
) {
    fun checkUrl(
        url: String,
        onFileFound: (downloadTask: DownloadTask) -> Unit,
        onError: (message: String) -> Unit
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (url.isNotEmpty()) {
                val new = DownloadTask.new(url)
                fileManager.getMimeType(url)
                onFileFound(new)
            } else {
                onError("Invalid url")
            }
        }, 1000)
    }
}
