package com.joshgm3z.downloader.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joshgm3z.downloader.data.DownloadTask
import com.joshgm3z.downloader.ui.theme.DownloaderTheme

@Preview
@Composable
private fun PreviewMainUi() {
    DownloaderTheme {
        Surface {
            MainUi()
        }
    }
}

@Composable
fun MainUiContainer() {
    Surface {
        MainUi()
    }
}

@Composable
private fun MainUi(downloads: List<DownloadTask> = DownloadTask.samples) {
    DownloadTaskList(downloads)
}

