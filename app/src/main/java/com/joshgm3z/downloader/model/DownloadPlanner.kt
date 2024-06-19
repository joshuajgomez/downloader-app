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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadPlannerProvider {
    @Binds
    abstract fun provideDownloadEvents(downloadPlanner: DownloadPlanner): DownloadEvents
}

@Singleton
class DownloadPlanner @Inject constructor(
    scope: CoroutineScope,
    private val downloadWorker: DownloadWorker,
    private val downloadTaskDao: DownloadTaskDao,
) : DownloadEvents {

    private var runningTask: DownloadTask? = null

    private val downloadTasksQueue = HashMap<Int, DownloadTask>()

    init {
        Logger.entry()
        scope.launch {
            downloadTaskDao.getAll().collectLatest {
                Logger.debug("downloadTasks = [${it}]")
                checkNewDownloads(it)
            }
        }
        scope.launch {
            downloadWorker.downloadTaskFlow.collectLatest {
                if (it == null) {
                    Logger.warn("DownloadTask is null")
                    return@collectLatest
                }

                if (it.state == DownloadState.COMPLETED) {
                    // prepare queue for next download
                    downloadTasksQueue.remove(it.id)
                    runningTask = null
                    doNextTask()
                    downloadTaskDao.update(it)
                }
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
        downloadWorker.download(downloadTask)
    }

    override fun taskUpdates(): StateFlow<DownloadTask?> = downloadWorker.downloadTaskFlow
}

