package com.joshgm3z.downloader.ui.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable

@Composable
fun CustomCard(content: @Composable ColumnScope.() -> Unit) {
    Card {
        content()
    }
}
