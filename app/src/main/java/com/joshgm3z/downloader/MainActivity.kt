package com.joshgm3z.downloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.joshgm3z.downloader.ui.MainUiContainer
import com.joshgm3z.downloader.ui.theme.DownloaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DownloaderTheme {
                MainUiContainer()
            }
        }
    }
}
