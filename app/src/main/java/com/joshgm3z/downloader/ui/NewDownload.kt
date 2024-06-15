package com.joshgm3z.downloader.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.joshgm3z.downloader.ui.common.CustomTextField
import com.joshgm3z.downloader.ui.common.LayoutId
import com.joshgm3z.downloader.ui.theme.DownloaderTheme

@Preview
@Composable
fun PreviewNewDownload() {
    DownloaderTheme {
        NewDownload()
    }
}

@Composable
fun NewDownload(onDownloadClick: (url: String) -> Unit = {}) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        ConstraintLayout(
            newDownloadConstraints(),
            Modifier.padding(10.dp)
        ) {
            Text(
                text = "Enter url for download", fontWeight = FontWeight.Bold,
                modifier = Modifier.layoutId(LayoutId.cardTitle)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.layoutId(LayoutId.close)
            )
            var text by remember { mutableStateOf("") }
            CustomTextField(
                text = text,
                onTextChanged = { text = it },
                modifier = Modifier.layoutId(LayoutId.textInput),
            )
            RecentUrlList(
                Modifier.layoutId(LayoutId.recentList)
            )
            Button(
                onClick = { onDownloadClick(text) },
                modifier = Modifier.layoutId(LayoutId.button)
            ) {
                Text(text = "Start Download")
            }
        }
    }
}

val recentUrlSamples = listOf(
    "https://filesamples.com/samples/video/webm/sample_960x400_ocean_with_audio.webm",
    "https://filesamples.com/samples/document/doc/sample2.doc",
    "https://filesamples.com/samples/video/webm/sample_640x360.webm",
    "https://filesamples.com/samples/image/gif/sample_5184%C3%973456.gif",
    "https://filesamples.com/samples/video/webm/sample_1920x1080.webm",
)

@Composable
fun RecentUrlList(modifier: Modifier) {
    LazyColumn(
        modifier
            .height(150.dp)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(recentUrlSamples) {
            UrlItem(it)
        }
    }
}

@Composable
fun UrlItem(url: String) {
    Text(
        text = url,
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(10.dp),
                color = colorScheme.primaryContainer
            )
            .padding(horizontal = 15.dp, vertical = 5.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

private fun newDownloadConstraints() = ConstraintSet {
    val title = createRefFor(LayoutId.cardTitle)
    val textInput = createRefFor(LayoutId.textInput)
    val button = createRefFor(LayoutId.button)
    val recentList = createRefFor(LayoutId.recentList)
    val close = createRefFor(LayoutId.close)

    constrain(title) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
    }
    constrain(close) {
        top.linkTo(parent.top)
        end.linkTo(parent.end)
    }
    constrain(textInput) {
        top.linkTo(title.bottom, 20.dp)
        start.linkTo(parent.start)
    }
    constrain(recentList) {
        top.linkTo(textInput.bottom, 20.dp)
        end.linkTo(parent.end)
    }
    constrain(button) {
        bottom.linkTo(parent.bottom)
        top.linkTo(recentList.bottom, 10.dp)
        end.linkTo(parent.end)
    }
}
