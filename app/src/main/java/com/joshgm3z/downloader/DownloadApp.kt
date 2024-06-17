package com.joshgm3z.downloader

import android.app.Application
import android.content.Intent
import com.joshgm3z.downloader.service.DownloadService
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DownloadApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.debug("starting downloadservice")
        Intent(this, DownloadService::class.java).apply {
            startService(this)
        }
    }
}