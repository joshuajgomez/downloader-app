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
fun PreviewDownloadTaskListContainer(paddingValues: PaddingValues = PaddingValues(10.dp)) {
    DownloaderTheme {
        DownloadTaskList()
    }
}

@Composable
fun DownloadTaskListContainer(
    downloadViewModel: DownloadViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 10.dp),
) {
    val listState = downloadViewModel.downloadTasks.collectAsState()
    DownloadTaskList(listState.value, paddingValues)
}

@Composable
fun DownloadTaskList(
    downloadTasks: List<DownloadTask> = DownloadTask.samples,
    paddingValues: PaddingValues = PaddingValues(horizontal = 10.dp),
) {
    if (downloadTasks.isEmpty()) {
        EmptyScreen()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = paddingValues,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            items(downloadTasks) {
                DownloadTaskItem(it)
            }
        }
    }
}