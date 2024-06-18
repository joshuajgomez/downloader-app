package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadWorker @Inject constructor() {

    suspend fun download(
        downloadTask: DownloadTask,
        onStateUpdate: (state: DownloadState, progress: Double) -> Unit
    ) {
        Logger.debug("downloadTask = [${downloadTask}]")
        // set runningTask to avoid parallel downloads
        onStateUpdate(DownloadState.RUNNING, 0.0)
        repeat(10) {
            onStateUpdate(DownloadState.RUNNING, (it + 1) * 10.0)
            sleep(1000)

        }
        onStateUpdate(DownloadState.COMPLETED, 100.0)
    }
}
