package com.joshgm3z.downloader.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.downloader.ui.common.DeleteIcon
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.ui.theme.jumperFamily
import com.joshgm3z.downloader.viewmodel.DeleteDownloadViewModel

@Preview
@Composable
fun PreviewTitleBar() {
    DownloaderTheme {
        TitleBar()
    }
}

@Composable
fun TitleBar(deleteDownloadViewModel: DeleteDownloadViewModel = hiltViewModel()) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 20.dp,
            bottomEnd = 20.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = colorScheme.primary)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Downzilla",
                fontSize = 25.sp,
                fontFamily = jumperFamily,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onPrimary
            )
            DeleteIcon {
                deleteDownloadViewModel.deleteAll()
            }
        }
    }
}
