package com.joshgm3z.downloader.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}