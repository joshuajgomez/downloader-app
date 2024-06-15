package com.joshgm3z.downloader.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.downloader.model.room.data.FileType
import com.joshgm3z.downloader.ui.theme.DownloaderTheme

@Preview(showBackground = true)
@Composable
fun PreviewFileIcon() {
    DownloaderTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(FileType.entries) {
                Text(text = it.name)
                FileIcon(it)
            }
        }
    }
}

@Composable
fun FileIcon(fileType: FileType) {
    Icon(
        imageVector = fileIcon(fileType),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .layoutId(LayoutId.icon)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary
            )
            .padding(10.dp)
    )
}

fun fileIcon(fileType: FileType) =
    when (fileType) {
        FileType.VIDEO -> Icons.Default.VideoCameraBack
        FileType.AUDIO -> Icons.Default.Audiotrack
        FileType.PDF -> Icons.Default.FilePresent
        FileType.TXT -> Icons.Default.TextSnippet
        FileType.EXE -> Icons.Default.Window
        FileType.UNKNOWN -> Icons.Default.FilePresent
    }