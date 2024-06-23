package com.joshgm3z.downloader.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.downloader.ui.theme.DownloaderTheme

@Preview
@Composable
fun PreviewCustomTextField() {
    DownloaderTheme {
        CustomTextField()
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    maxLines: Int = 3,
    onTextChanged: (text: String) -> Unit = {},
) {
    BasicTextField(
        modifier = modifier
            .background(
                color = colorScheme.outline.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.extraLarge,
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
            .layoutId(LayoutId.textInput),
        value = text,
        onValueChange = {
            if ("\n" !in it) onTextChanged(it)
        },
        cursorBrush = SolidColor(colorScheme.primary),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 20.sp,
        ),
        maxLines = maxLines
    )
}