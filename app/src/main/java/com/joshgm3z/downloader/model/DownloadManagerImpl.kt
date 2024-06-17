package com.joshgm3z.downloader.model

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
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadManagerProvider {
    @Binds
    abstract fun provideDownloadManager(
        downloadManagerImpl: DownloadManagerImpl
    ): DownloadEvents
}

@Singleton
class DownloadManagerImpl @Inject constructor(
    private val scope: CoroutineScope,
    private val downloadTaskDao: DownloadTaskDao,
) : DownloadEvents {

    init {
        Logger.entry()
        scope.launch {
            downloadTaskDao.getAll().collectLatest { downloadTasks ->
                Logger.debug("downloadTasks = [${downloadTasks}]")
                checkNewDownloads(downloadTasks)
            }
        }
    }

    private var runningTask: DownloadTask? = null

    private val pendingTasksList = hashMapOf<Int, DownloadTask>()

    private val downloadTaskFlow: MutableStateFlow<DownloadTask?> = MutableStateFlow(null)

    private fun checkNewDownloads(downloadTasks: List<DownloadTask>) {
        Logger.debug("downloadTasks = [${downloadTasks}]")
        downloadTasks.forEach {
            if (it.state == DownloadState.PENDING) {
                if (!pendingTasksList.contains(it.id)) {
                    Logger.debug("Adding to pending queue: ${it.id}")
                    addToQueue(it)
                }
            }
        }
    }

    private fun addToQueue(downloadTask: DownloadTask) {
        Logger.debug("downloadTask = [${downloadTask}]")
        pendingTasksList[downloadTask.id] = downloadTask
        checkQueue()
    }

    private fun checkQueue() {
        Logger.entry()
        if (runningTask != null) {
            Logger.debug("Task already running: $runningTask")
            return
        }
        pendingTasksList.values.forEach { downloadTask ->
            updateTaskState(DownloadState.RUNNING, downloadTask)
            download(downloadTask)
        }
    }

    private fun updateTaskState(state: DownloadState, downloadTask: DownloadTask) {
        Logger.debug("state = [${state}], downloadTask = [${downloadTask}]")
        scope.launch {
            downloadTaskDao.update(
                downloadTask.copy(
                    state = state
                )
            )
        }
    }

    private fun download(downloadTask: DownloadTask) {
        Logger.debug("downloadTask = [${downloadTask}]")
        scope.launch {
            // set runningTask to avoid parallel downloads
            runningTask = downloadTask
            notifyDownloadState(DownloadState.RUNNING, 0.0)
            repeat(10) {
                sleep(1000)
                notifyDownloadState(
                    state = DownloadState.RUNNING,
                    progress = (it + 1) * 10.0
                )
            }
            notifyDownloadState(DownloadState.COMPLETED, 100.0)
            // prepare queue for next download
            pendingTasksList.remove(downloadTask.id)
            runningTask = null
            updateTaskState(DownloadState.COMPLETED, downloadTask)
            checkQueue()
        }
    }

    private fun notifyDownloadState(state: DownloadState, progress: Double) {
        scope.launch {
            downloadTaskFlow.emit(
                runningTask?.copy(
                    state = state,
                    progress = progress
                )
            )
        }
    }

    override fun taskUpdates(): StateFlow<DownloadTask?> = downloadTaskFlow.asStateFlow()

}

