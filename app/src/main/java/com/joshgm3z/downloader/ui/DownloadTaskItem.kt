package com.joshgm3z.downloader.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.joshgm3z.downloader.model.room.data.DownloadState
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.ui.common.FileIcon
import com.joshgm3z.downloader.ui.common.LayoutId
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.ui.theme.Green40
import com.joshgm3z.downloader.utils.sizeText

@Preview
@Composable
private fun PreviewDownloadTaskItem() {
    DownloaderTheme {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(DownloadState.entries) {
                Text(text = it.name, color = Color.White)
                DownloadTaskItem(
                    DownloadTask.sample.copy(
                        state = it,
                    )
                )
            }
        }
    }
}

@Composable
fun DownloadTaskItem(downloadTask: DownloadTask = DownloadTask.sample) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        ConstraintLayout(
            downloadTaskConstraints(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
        ) {

            FileIcon(downloadTask.fileType)

            Text(
                text = downloadTask.filename,
                modifier = Modifier
                    .layoutId(LayoutId.fileName)
                    .fillMaxWidth(0.7f),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val state = downloadTask.state
            AnimatedVisibility(
                visible = state == DownloadState.RUNNING ||
                        state == DownloadState.PAUSED,
                modifier = Modifier.layoutId(LayoutId.progress)
            ) {
                LinearProgressIndicator(
                    progress = { downloadTask.progress.toFloat() / 100 },
                    trackColor = colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }

            Text(
                text = downloadTask.url,
                modifier = Modifier
                    .layoutId(LayoutId.url)
                    .fillMaxWidth(0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            StateIndicator(
                Modifier.layoutId(LayoutId.progressText),
                downloadTask
            )

            Row(
                modifier = Modifier.layoutId(LayoutId.size)
            ) {
                AnimatedVisibility(
                    visible = state == DownloadState.RUNNING ||
                            state == DownloadState.PAUSED
                ) {
                    Text(
                        text = "${downloadTask.currentSize.sizeText()} of ",
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = downloadTask.totalSize.sizeText(),
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}

@Composable
fun StateIndicator(
    modifier: Modifier = Modifier,
    downloadTask: DownloadTask
) {
    Row(modifier) {
        when (downloadTask.state) {
            DownloadState.PENDING -> {
                Icon(Icons.Default.AccessTime, null)
            }

            DownloadState.RUNNING -> {
                Text(
                    text = "${downloadTask.progress.toInt()}%",
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }

            DownloadState.PAUSED -> {
                Text(
                    text = "Paused ${downloadTask.progress.toInt()}%",
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                Icon(Icons.Default.Pause, null)
            }

            DownloadState.COMPLETED -> {
                Icon(
                    Icons.Default.CheckCircle, null,
                    tint = Green40
                )
            }

            DownloadState.FAILED -> {
                Text(
                    text = "Failed",
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.error
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    Icons.Default.ErrorOutline, null,
                    tint = colorScheme.error
                )
            }
        }
    }
}

private fun downloadTaskConstraints(): ConstraintSet {
    return ConstraintSet {
        val fileName = createRefFor(LayoutId.fileName)
        val icon = createRefFor(LayoutId.icon)
        val progress = createRefFor(LayoutId.progress)
        val url = createRefFor(LayoutId.url)
        val close = createRefFor(LayoutId.close)
        val progressText = createRefFor(LayoutId.progressText)
        val size = createRefFor(LayoutId.size)

        constrain(icon) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(fileName) {
            top.linkTo(parent.top)
            start.linkTo(icon.end, 10.dp)
        }

        constrain(progress) {
            top.linkTo(url.bottom, 5.dp)
            start.linkTo(fileName.start)
        }

        constrain(url) {
            top.linkTo(fileName.bottom)
            start.linkTo(fileName.start)
        }
        constrain(close) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
        constrain(size) {
            start.linkTo(url.start)
            top.linkTo(progressText.top)
        }
        constrain(progressText) {
            end.linkTo(parent.end)
            top.linkTo(progress.bottom, 5.dp)
        }
    }
}