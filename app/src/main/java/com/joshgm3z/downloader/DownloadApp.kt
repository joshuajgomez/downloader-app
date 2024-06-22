package com.joshgm3z.downloader

import android.app.Application
import android.content.Intent
import com.joshgm3z.downloader.service.DownzillaService
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DownloadApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.debug("starting downloadservice")
        Intent(this, DownzillaService::class.java).apply {
            startService(this)
        }
    }
}