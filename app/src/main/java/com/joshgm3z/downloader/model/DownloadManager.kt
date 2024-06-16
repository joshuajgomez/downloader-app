package com.joshgm3z.downloader.model

import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadManager @Inject constructor(
    private val scope: CoroutineScope,
    private val downloadTaskDao: DownloadTaskDao,
) {

    private val pendingTasksList = hashMapOf<Int, DownloadTask>()

    val downloadTaskFlow: MutableStateFlow<DownloadTask?> = MutableStateFlow(null)

    fun addToQueue(downloadTask: DownloadTask) {
        Logger.debug("downloadTask = [${downloadTask}]")
        pendingTasksList[downloadTask.id] = downloadTask
        checkQueue()
    }

    private fun checkNewDownloads(downloadTasks: List<DownloadTask>) {
        downloadTasks.forEach {
            if (it.state == DownloadState.PENDING) {
                if (!pendingTasksList.contains(it.id)) {
                    Logger.debug("Adding to pending tasks: ${it.id}")
                    pendingTasksList[it.id] = it
                }
            }
        }
        checkQueue()
    }

    private fun checkQueue() {
        Logger.entry()
        pendingTasksList.firstNotNullOf {
            val downloadTask = it.value
            updateTaskState(downloadTask)
            download(downloadTask)
        }
    }

    private fun updateTaskState(downloadTask: DownloadTask) {
        Logger.debug("downloadTask = [${downloadTask}]")
        scope.launch {
            downloadTaskDao.update(
                downloadTask.copy(
                    state = DownloadState.RUNNING
                )
            )
        }
    }

    private fun download(downloadTask: DownloadTask) {
        Logger.debug("downloadTask = [${downloadTask}]")
        scope.launch {
            downloadTaskFlow.emit(
                downloadTask.copy(
                    state = DownloadState.RUNNING,
                    progress = 0.0
                )
            )
            repeat(10) {
                sleep(1000)
                downloadTaskFlow.emit(
                    downloadTask.copy(
                        state = DownloadState.RUNNING,
                        progress = it * 10.0
                    )
                )
            }
            downloadTaskFlow.emit(
                downloadTask.copy(
                    state = DownloadState.COMPLETED,
                    progress = 100.0,
                )
            )
        }
    }

}
