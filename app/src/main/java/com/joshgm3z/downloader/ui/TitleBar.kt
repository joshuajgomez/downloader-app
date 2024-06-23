package com.joshgm3z.downloader.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.downloader.ui.common.CustomTextField
import com.joshgm3z.downloader.ui.common.DarkPreview
import com.joshgm3z.downloader.ui.common.DeleteIcon
import com.joshgm3z.downloader.ui.common.LayoutId
import com.joshgm3z.downloader.ui.theme.DownloaderTheme
import com.joshgm3z.downloader.ui.theme.jumperFamily
import com.joshgm3z.downloader.viewmodel.DeleteDownloadViewModel

@DarkPreview
@Composable
fun PreviewTitleBar() {
    DownloaderTheme {
        TitleBar()
    }
}

@Composable
fun TitleBar(deleteDownloadViewModel: DeleteDownloadViewModel = hiltViewModel()) {
    ConstraintLayout(
        titleBarConstraints(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Text(
            text = "Downzilla",
            fontSize = 25.sp,
            fontFamily = jumperFamily,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
            modifier = Modifier.layoutId(LayoutId.title)
        )
        DeleteIcon {
            deleteDownloadViewModel.deleteAll()
        }
        CustomTextField()
    }
}

private fun titleBarConstraints() = ConstraintSet {
    val title = createRefFor(LayoutId.title)
    val delete = createRefFor(LayoutId.deleteIcon)
    val search = createRefFor(LayoutId.textInput)
    constrain(title) {
        top.linkTo(parent.top, 5.dp)
        start.linkTo(parent.start)
    }
    constrain(delete){
        top.linkTo(title.top)
        bottom.linkTo(title.bottom)
        end.linkTo(parent.end)
    }
    constrain(search){
        top.linkTo(title.bottom, 20.dp)
        start.linkTo(title.start)
    }
}
