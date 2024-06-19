package com.joshgm3z.downloader.utils

import android.annotation.SuppressLint
import java.text.CharacterIterator
import java.text.StringCharacterIterator

@SuppressLint("DefaultLocale")
fun Long.sizeText(): String {
    var bytes = this
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return String.format("%.1f %cB", bytes / 1000.0, ci.current())
}