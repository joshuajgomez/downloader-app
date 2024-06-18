package com.joshgm3z.downloader.model

import androidx.compose.runtime.mutableStateMapOf
import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadManagerProvider{
    @Binds
    abstract fun provideDownloadEvents(downloadManager: DownloadManager): DownloadEvents
}

@Singleton
class DownloadManager @Inject constructor(
    private val scope: CoroutineScope,
    private val downloadWorker: DownloadWorker,
    private val downloadTaskDao: DownloadTaskDao,
) : DownloadEvents {

    private val downloadTaskFlow = MutableStateFlow<DownloadTask?>(null)

    private var runningTask: DownloadTask? = null

    private val downloadTasksQueue = mutableStateMapOf<Int, DownloadTask>()

    init {
        Logger.entry()
        scope.launch {
            downloadTaskDao.getAll().collectLatest {
                Logger.debug("downloadTasks = [${it}]")
                checkNewDownloads(it)
            }
        }
    }

    private fun checkNewDownloads(downloadTasks: List<DownloadTask>) {
        Logger.debug("downloadTasks = [${downloadTasks}]")
        downloadTasks.forEach {
            if (it.state == DownloadState.PENDING) {
                if (!isInQueue(it)) {
                    Logger.debug("Adding to queue: ${it.id}")
                    addTaskToQueue(it)
                }
            }
        }
    }

    private fun addTaskToQueue(downloadTask: DownloadTask) {
        if (isInQueue(downloadTask)) {
            Logger.warn("Already in queue: $downloadTask")
            return
        }
        Logger.debug("downloadTask = [${downloadTask}]")
        downloadTasksQueue[downloadTask.id] = downloadTask
        doNextTask()
    }

    private fun isInQueue(downloadTask: DownloadTask): Boolean =
        downloadTasksQueue.contains(downloadTask.id)

    private fun doNextTask() {
        Logger.entry()
        if (runningTask != null) {
            Logger.debug("Task already running: $runningTask")
            return
        }
        if (downloadTasksQueue.values.isEmpty()) {
            Logger.warn("Queue empty")
            return
        }
        val downloadTask = downloadTasksQueue.values.first()
        runningTask = downloadTask
        scope.launch {
            downloadWorker.download(downloadTask) { state, progress ->
                notifyState(downloadTask.copy(state = state, progress = progress))

                if (state == DownloadState.COMPLETED) {
                    // prepare queue for next download
                    downloadTasksQueue.remove(downloadTask.id)
                    runningTask = null
                    doNextTask()
                }
            }
        }
    }

    private fun notifyState(downloadTask: DownloadTask) {
        scope.launch {
            downloadTaskFlow.emit(
                downloadTask
            )
            if (downloadTask.state == DownloadState.COMPLETED) {
                downloadTaskDao.update(downloadTask)
            }
        }
    }

    override fun taskUpdates(): StateFlow<DownloadTask?> = downloadTaskFlow.asStateFlow()
}

