package com.joshgm3z.downloader.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.viewmodel.DownloadViewModel

@Preview
@Composable
fun PreviewDownloadTaskList() {
    DownloaderTheme {
        DownloadTaskList()
    }
}

@Composable
fun DownloadTaskList(
    downloadViewModel: DownloadViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 10.dp),
) {
    val listState = downloadViewModel.downloadTasks.collectAsState()
    val downloadTasks = listState.value
    if (downloadTasks.isEmpty()) {
        EmptyScreen()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = paddingValues
        ) {
            items(downloadTasks) {
                DownloadTaskItem(it)
            }
        }
    }
}