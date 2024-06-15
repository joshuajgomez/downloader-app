package com.joshgm3z.downloader.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.ui.common.CustomCard
import com.joshgm3z.downloader.ui.common.FileIcon
import com.joshgm3z.downloader.ui.common.LayoutId


@Preview
@Composable
private fun PreviewDownloadTaskItemExpanded() {
    DownloaderTheme {
        DownloadTaskItemExpanded()
    }
}

@Composable
fun DownloadTaskItemExpanded(downloadTask: DownloadTask = DownloadTask.sample) {
    CustomCard {
        ConstraintLayout(
            downloadTaskConstraints(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
        ) {

            Text(
                text = "Downloading",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.layoutId(LayoutId.cardTitle)
            )

            FileIcon(downloadTask.fileType)

            Text(
                text = downloadTask.filename,
                modifier = Modifier
                    .layoutId(LayoutId.fileName)
                    .fillMaxWidth(0.8f),
                fontWeight = FontWeight.Bold,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            ProgressInfo(
                modifier = Modifier.layoutId(LayoutId.progress),
                progress = downloadTask.progress.toFloat(),
            )

            Text(
                text = downloadTask.url,
                modifier = Modifier
                    .layoutId(LayoutId.url)
                    .fillMaxWidth(0.8f),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${downloadTask.currentSize.toInt()}MB of ${downloadTask.totalSize.toInt()}MB",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.layoutId(LayoutId.size)
            )

            ButtonBox(modifier = Modifier.layoutId(LayoutId.button))

        }
    }
}

@Composable
fun ButtonBox(modifier: Modifier = Modifier) {
    Column(modifier) {
        Button(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Pause, null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Pause")
        }
        OutlinedButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Delete, null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Delete")
        }
    }
}

@Composable
fun ProgressInfo(modifier: Modifier = Modifier, progress: Float) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(95.dp)
        )
        Text(
            text = "${progress.toInt()}%",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    }
}

private fun downloadTaskConstraints(): ConstraintSet {
    return ConstraintSet {
        val fileName = createRefFor(LayoutId.fileName)
        val icon = createRefFor(LayoutId.icon)
        val progress = createRefFor(LayoutId.progress)
        val url = createRefFor(LayoutId.url)
        val close = createRefFor(LayoutId.close)
        val size = createRefFor(LayoutId.size)
        val button = createRefFor(LayoutId.button)
        val title = createRefFor(LayoutId.cardTitle)

        constrain(title) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(progress) {
            top.linkTo(title.bottom, 10.dp)
            start.linkTo(parent.start)
        }

        constrain(icon) {
            top.linkTo(progress.bottom, 15.dp)
            start.linkTo(parent.start)
        }

        constrain(fileName) {
            top.linkTo(icon.top)
            start.linkTo(icon.end, 10.dp)
        }

        constrain(url) {
            top.linkTo(fileName.bottom, 5.dp)
            start.linkTo(fileName.start)
        }
        constrain(close) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
        constrain(size) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }
        constrain(button) {
            end.linkTo(parent.end)
            top.linkTo(size.bottom, 10.dp)
        }

    }
}