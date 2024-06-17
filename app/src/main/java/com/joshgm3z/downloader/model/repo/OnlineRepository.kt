package com.joshgm3z.downloader.model.repo;

import android.os.Handler
import android.os.Looper
import com.joshgm3z.downloader.model.room.data.DownloadTask
import javax.inject.Inject

class OnlineRepository @Inject constructor() {
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
