package com.joshgm3z.downloader.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.joshgm3z.downloader.data.DownloadTask
import com.joshgm3z.downloader.data.FileType
import com.joshgm3z.downloader.ui.common.CustomCard
import com.joshgm3z.downloader.ui.theme.DownloaderTheme


@Preview
@Composable
private fun PreviewDownloadTaskItem() {
    DownloaderTheme {
        DownloadTaskItem()
    }
}

@Composable
fun DownloadTaskItem(downloadTask: DownloadTask = DownloadTask.sample) {
    CustomCard {
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
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            LinearProgressIndicator(
                progress = downloadTask.progress.toFloat(),
                modifier = Modifier.layoutId(LayoutId.progress)
            )

            Text(
                text = downloadTask.url,
                modifier = Modifier
                    .layoutId(LayoutId.url)
                    .fillMaxWidth(0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.layoutId(LayoutId.close)
            )

            Text(
                text = "${downloadTask.progress.toInt()}%",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.layoutId(LayoutId.progressText)
            )

        }
    }
}

fun fileIcon(fileType: FileType) =
    when (fileType) {
        FileType.VIDEO -> Icons.Default.VideoCameraBack
        FileType.AUDIO -> Icons.Default.TextFields
        FileType.PDF -> Icons.Default.TextFields
        FileType.TXT -> Icons.Default.TextFields
        FileType.EXE -> Icons.Default.TextFields
        FileType.UNKNOWN -> Icons.Default.TextFields
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

enum class LayoutId {
    fileName,
    icon,
    progress,
    url,
    close,
    progressText,
}

private fun downloadTaskConstraints(): ConstraintSet {
    return ConstraintSet {
        val fileName = createRefFor(LayoutId.fileName)
        val icon = createRefFor(LayoutId.icon)
        val progress = createRefFor(LayoutId.progress)
        val url = createRefFor(LayoutId.url)
        val close = createRefFor(LayoutId.close)
        val progressText = createRefFor(LayoutId.progressText)

        constrain(icon) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(fileName) {
            top.linkTo(parent.top)
            start.linkTo(icon.end, 10.dp)
        }

        constrain(progress) {
            top.linkTo(fileName.bottom, 5.dp)
            start.linkTo(fileName.start)
        }

        constrain(url) {
            top.linkTo(progress.bottom, 5.dp)
            start.linkTo(fileName.start)
        }
        constrain(close) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
        constrain(progressText) {
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
    }
}