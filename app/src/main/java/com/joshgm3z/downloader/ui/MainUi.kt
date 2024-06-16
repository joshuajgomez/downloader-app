package com.joshgm3z.downloader.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.downloader.ui.common.CloseIcon
import com.joshgm3z.downloader.ui.common.DeleteIcon
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.utils.Logger

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewMainUi() {
    DownloaderTheme {
        Scaffold(
            topBar = { TitleBar() },
            floatingActionButton = {
                AddDownloadButton()
            },
        ) {
            DownloadTaskList(paddingValues = it)
            /*ModalBottomSheet(onDismissRequest = { }) {
                NewDownload()
            }*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi() {
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TitleBar() },
        floatingActionButton = {
            AddDownloadButton {
                Logger.debug("new download button click")
                showBottomSheet = true
            }
        },
    ) {
        DownloadTaskListContainer(paddingValues = it)
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                content = {
                    NewDownload {
                        showBottomSheet = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddDownloadButton(onAddClick: () -> Unit = {}) {
    ExtendedFloatingActionButton(
        text = { Text("New Download") },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
        onClick = onAddClick
    )

}

