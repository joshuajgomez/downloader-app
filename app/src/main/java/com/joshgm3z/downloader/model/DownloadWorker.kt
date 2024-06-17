package com.joshgm3z.downloader.model

import androidx.compose.runtime.mutableStateMapOf
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
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadEventsProvider() {
    @Binds
    abstract fun provideDownloadEvents(
        downloadWorker: DownloadWorker
    ): DownloadEvents
}

@Singleton
class DownloadWorker @Inject constructor(
    private val scope: CoroutineScope,
) : DownloadEvents {

    private var runningTask: DownloadTask? = null

    private val downloadTasksQueue = mutableStateMapOf<Int, DownloadTask>()

    private val downloadTaskFlow = MutableStateFlow<DownloadTask?>(null)

    override fun addTaskToQueue(downloadTask: DownloadTask) {
        if (isInQueue(downloadTask)) {
            Logger.warn("Already in queue: $downloadTask")
            return
        }
        Logger.debug("downloadTask = [${downloadTask}]")
        downloadTasksQueue[downloadTask.id] = downloadTask
        checkQueue()
    }

    override fun isInQueue(downloadTask: DownloadTask): Boolean =
        downloadTasksQueue.contains(downloadTask.id)

    override fun taskUpdates(): StateFlow<DownloadTask?> =
        downloadTaskFlow.asStateFlow()

    private fun checkQueue() {
        Logger.entry()
        if (runningTask != null) {
            Logger.debug("Task already running: $runningTask")
            return
        }
        downloadTasksQueue.values.forEach { downloadTask ->
            download(downloadTask)
        }
    }

    private fun download(downloadTask: DownloadTask) {
        Logger.debug("downloadTask = [${downloadTask}]")
        scope.launch {
            // set runningTask to avoid parallel downloads
            runningTask = downloadTask
            notifyDownloadState(DownloadState.RUNNING, 0.0)
            repeat(10) {
                Thread.sleep(1000)
                notifyDownloadState(
                    state = DownloadState.RUNNING,
                    progress = (it + 1) * 10.0
                )
            }
            notifyDownloadState(DownloadState.COMPLETED, 100.0)
            // prepare queue for next download
            downloadTasksQueue.remove(downloadTask.id)
            runningTask = null
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
}