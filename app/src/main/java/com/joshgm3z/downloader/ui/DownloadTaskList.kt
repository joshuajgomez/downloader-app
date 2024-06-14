package com.joshgm3z.downloader.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.downloader.data.DownloadTask
import com.joshgm3z.downloader.ui.theme.DownloaderTheme

@Preview
@Composable
fun PreviewDownloadTaskList() {
    DownloaderTheme {
        DownloadTaskList()
    }
}

@Composable
fun DownloadTaskList(downloads: List<DownloadTask> = DownloadTask.samples) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(downloads) {
            DownloadTaskItem(it)
        }
    }
}