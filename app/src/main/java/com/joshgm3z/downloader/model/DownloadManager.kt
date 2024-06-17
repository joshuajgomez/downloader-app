package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadManager @Inject constructor(
    scope: CoroutineScope,
    private val downloadEvents: DownloadEvents,
    private val downloadTaskDao: DownloadTaskDao,
) {

    init {
        Logger.entry()
        scope.launch {
            downloadTaskDao.getAll().collectLatest {
                Logger.debug("downloadTasks = [${it}]")
                checkNewDownloads(it)
            }
        }
        scope.launch {
            downloadEvents.taskUpdates().collectLatest {
                if (it?.state == DownloadState.COMPLETED) {
                    downloadTaskDao.update(it)
                }
            }
        }
    }

    private fun checkNewDownloads(downloadTasks: List<DownloadTask>) {
        Logger.debug("downloadTasks = [${downloadTasks}]")
        downloadTasks.forEach {
            if (it.state == DownloadState.PENDING) {
                if (!downloadEvents.isInQueue(it)) {
                    Logger.debug("Adding to queue: ${it.id}")
                    downloadEvents.addTaskToQueue(it)
                }
            }
        }
    }

}

