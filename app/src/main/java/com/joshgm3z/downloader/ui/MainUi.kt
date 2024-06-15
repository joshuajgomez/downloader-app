package com.joshgm3z.downloader.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainUi(downloads: List<DownloadTask> = DownloadTask.samples) {
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TitleBar() },
        floatingActionButton = {
            AddDownloadButton {
                showBottomSheet = true
            }
        },
    ) {
        DownloadTaskList(it, downloads)
        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                NewDownload {
                    showBottomSheet = false
                }
            }
        }
    }
}

@Composable
fun TitleBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Downloader",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
    }
}

@Composable
fun AddDownloadButton(onAddClick: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text("New Download") },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
        onClick = onAddClick
    )

}

