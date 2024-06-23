package com.joshgm3z.downloader.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.ui.common.DarkPreview
import com.joshgm3z.downloader.ui.common.FileIcon
import com.joshgm3z.downloader.ui.common.LayoutId
import com.joshgm3z.downloader.ui.common.ThemePreviews
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.utils.relativeTime
import com.joshgm3z.downloader.utils.sizeText

@ThemePreviews
@Composable
fun DownloadItemPreview() {
    DownloaderTheme {
        Surface {
            LazyColumn {
                items(DownloadState.entries) {
                    Column {
                        DownloadItem(downloadTask = DownloadTask.sample.copy(state = it))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadItem(downloadTask: DownloadTask) {
    ConstraintLayout(
        simpleDownloadItemConstraints(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ) {
        FileIcon(
            downloadTask.fileType,
            Modifier.size(50.dp)
        )
        Text(
            text = downloadTask.filename,
            modifier = Modifier
                .layoutId(LayoutId.fileName)
                .fillMaxWidth(0.8f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = colorScheme.onSurface,
            fontSize = 20.sp
        )
        BriefMetadata(
            Modifier
                .layoutId(LayoutId.metadata)
                .fillMaxWidth(0.8f),
            downloadTask
        )
    }
}

@Composable
fun BriefMetadata(
    modifier: Modifier,
    downloadTask: DownloadTask
) {
    Row(
        modifier, horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (downloadTask.state) {
            DownloadState.PENDING -> {
                PendingIcon()
                Info(downloadTask.totalSize.sizeText())
            }

            DownloadState.PAUSED -> {
                Info("Paused")
                PauseIcon()
                Info("${downloadTask.progress}%")
                Dot()
                Info(
                    downloadTask.currentSize.sizeText()
                )
                Info(
                    " of ${downloadTask.totalSize.sizeText()}"
                )
            }

            DownloadState.RUNNING -> {
                if (downloadTask.totalSize == 0L) {
                    Info("Downloading")
                } else {
                    Info("${downloadTask.progress}%")
                }
                Dot()
                Info(
                    downloadTask.currentSize.sizeText()
                )
                if (downloadTask.totalSize > 0)
                    Info(
                        " of ${downloadTask.totalSize.sizeText()}"
                    )
            }

            DownloadState.COMPLETED -> {
                Info(downloadTask.totalSize.sizeText())
                Dot()
                Info(downloadTask.timeCompleted.relativeTime())
            }

            DownloadState.FAILED -> {
                Info("Failed")
                FailedIcon()
            }

            DownloadState.STOPPED -> {
                Info("Cancelled")
            }

        }
    }
}

@Composable
fun Info(text: String) {
    Text(
        text = text,
        color = colorScheme.onSurface.copy(alpha = 0.7f)
    )
}

@Composable
fun FailedIcon() {
    Icon(
        imageVector = Icons.Default.Error,
        contentDescription = null,
        tint = colorScheme.error,
        modifier = Modifier.size(18.dp)
    )
}

@Composable
fun PendingIcon() {
    Icon(
        imageVector = Icons.Default.AccessTime,
        contentDescription = null,
        tint = colorScheme.primary,
        modifier = Modifier.size(18.dp)
    )
}

@Composable
fun PauseIcon() {
    Icon(
        imageVector = Icons.Default.Pause,
        contentDescription = null,
        tint = colorScheme.onSurface,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun Dot() {
    Text(text = "•", color = colorScheme.onSurface)
}

fun simpleDownloadItemConstraints() = ConstraintSet {
    val icon = createRefFor(LayoutId.icon)
    val filename = createRefFor(LayoutId.fileName)
    val metadata = createRefFor(LayoutId.metadata)
    constrain(icon) {
        start.linkTo(parent.start, 10.dp)
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
    }
    constrain(filename) {
        start.linkTo(icon.end, 10.dp)
        top.linkTo(icon.top)
    }
    constrain(metadata) {
        start.linkTo(filename.start)
        top.linkTo(filename.bottom, 5.dp)
    }
}
