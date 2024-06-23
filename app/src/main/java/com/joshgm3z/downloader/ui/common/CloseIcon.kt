package com.joshgm3z.downloader.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId

@Composable
fun CloseIcon(modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null
        )
    }
}

@Composable
fun DeleteIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.layoutId(LayoutId.deleteIcon),
        onClick = { onClick() }) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = colorScheme.onSurface
        )
    }
}
