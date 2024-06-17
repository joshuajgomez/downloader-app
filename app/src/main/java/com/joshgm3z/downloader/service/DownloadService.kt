package com.joshgm3z.downloader.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.joshgm3z.downloader.model.DownloadManager
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {

    @Inject
    lateinit var downloadManager: DownloadManager

    override fun onCreate() {
        super.onCreate()
        Logger.entry()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}