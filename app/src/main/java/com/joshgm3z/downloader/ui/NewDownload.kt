package com.joshgm3z.downloader.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.ui.common.CustomCard
import com.joshgm3z.downloader.ui.common.CustomTextField
import com.joshgm3z.downloader.ui.common.LayoutId
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.ui.theme.Green40
import com.joshgm3z.downloader.viewmodel.AddDownloadViewModel
import com.joshgm3z.downloader.viewmodel.AddUiState

@Preview(showBackground = true)
@Composable
fun PreviewNewDownload() {
    DownloaderTheme {
        NewDownload()
    }
}

@Composable
fun NewDownload(
    addDownloadViewModel: AddDownloadViewModel = hiltViewModel(),
    closeSheet: () -> Unit={}
) {
    ConstraintLayout(
        newDownloadConstraints(),
        Modifier.padding(horizontal = 10.dp)
    ) {
        Text(
            text = "New download",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.layoutId(LayoutId.cardTitle)
        )

        var text by remember { mutableStateOf("") }
        Content(
            modifier = Modifier.layoutId(LayoutId.recentList),
            addDownloadViewModel,
            onSuggestionsClick = {
                text = it
                addDownloadViewModel.onUrlEntered(text)
            },
            closeSheet = closeSheet
        )

        Column(
            modifier = Modifier
                .layoutId(LayoutId.button),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                text = text,
                onTextChanged = {
                    text = it
                    addDownloadViewModel.onUrlEntered(text)
                },
                maxLines = 1
            )
            Button(
                onClick = {
                    addDownloadViewModel.onDownloadClick()
                },
                modifier = Modifier
                    .padding(10.dp),
                enabled = addDownloadViewModel.downloadTask != null
            ) {
                Text(text = "Start Download")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    addViewModel: AddDownloadViewModel,
    onSuggestionsClick: (url: String) -> Unit,
    closeSheet: () -> Unit
) {
    CustomCard(
        modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        val uiState = addViewModel.uiState.collectAsState()
        when (uiState.value) {
            is AddUiState.Init -> {
                RecentUrlList {
                    onSuggestionsClick(it)
                }
            }

            is AddUiState.Fetching -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = (uiState.value as AddUiState.Fetching).message)
                }
            }

            is AddUiState.Metadata -> {
                FileMetadata(downloadTask = (uiState.value as AddUiState.Metadata).downloadTask)
            }

            is AddUiState.Added -> {
                Added()
            }

            is AddUiState.Close -> {
                closeSheet()
            }

            is AddUiState.Error -> {
                UrlError((uiState.value as AddUiState.Error).message)
            }
        }
    }
}

@Composable
fun UrlError(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = colorScheme.error
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = message)
    }
}

@Composable
fun Added() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Green40
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Download added")
    }
}

@Composable
fun FileMetadata(modifier: Modifier = Modifier, downloadTask: DownloadTask) {
    Column(modifier = modifier.padding(10.dp)) {
        InfoTag("File name", downloadTask.filename)
        val metadataList = linkedMapOf(
            "File size" to downloadTask.totalSize.toString(),
            "File type" to downloadTask.fileType.name,
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(metadataList.keys.toList()) {
                InfoTag(it, metadataList[it].toString())
            }
        }
    }
}

@Composable
fun InfoTag(label: String, value: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
fun RecentUrlList(onUrlClick: (url: String) -> Unit = {}) {
    LazyColumn(
        Modifier
            .height(150.dp)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(recentUrlSamples) {
            Column {
                UrlItem(it) {
                    onUrlClick(it)
                }
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun UrlItem(url: String, onUrlClick: () -> Unit) {
    Text(
        text = url,
        modifier = Modifier
            .clickable { onUrlClick() },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

private fun newDownloadConstraints() = ConstraintSet {
    val title = createRefFor(LayoutId.cardTitle)
    val button = createRefFor(LayoutId.button)
    val recentList = createRefFor(LayoutId.recentList)

    constrain(title) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
    constrain(recentList) {
        top.linkTo(title.bottom, 20.dp)
        start.linkTo(parent.start)
    }
    constrain(button) {
        top.linkTo(recentList.bottom, 10.dp)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom, 10.dp)
    }
}

val recentUrlSamples = listOf(
    "https://filesamples.com/samples/video/webm/sample_960x400_ocean_with_audio.webm",
    "https://filesamples.com/samples/document/doc/sample2.doc",
    "https://filesamples.com/samples/video/webm/sample_640x360.webm",
    "https://filesamples.com/samples/image/gif/sample_5184%C3%973456.gif",
    "https://filesamples.com/samples/video/webm/sample_1920x1080.webm",
)
