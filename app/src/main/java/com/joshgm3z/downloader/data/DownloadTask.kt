package com.joshgm3z.downloader.data

import android.net.Uri
import kotlin.random.Random

data class DownloadTask(
    val url: String,
    val filename: String,
    val totalSize: Double,
    val currentSize: Double,
    val progress: Double,
    val downloadState: DownloadState,
    val fileType: FileType,
) {
    companion object {
        val sample: DownloadTask
            get() {
                val url = sampleUrls.random()
                val filename = url.fileName()
                val totalSize = sampleFileSizes.random()
                val currentSize = totalSize - Random.nextDouble(totalSize)
                return DownloadTask(
                    url = url,
                    filename = url.fileName(),
                    fileType = filename.fileType(),
                    totalSize = totalSize,
                    currentSize = currentSize,
                    progress = (currentSize / totalSize) * 100,
                    downloadState = DownloadState.entries.random(),
                )
            }

        val samples: List<DownloadTask>
            get() {
                val samples = mutableListOf<DownloadTask>()
                repeat(10) {
                    samples.add(sample)
                }
                return samples
            }
    }
}

private fun String.fileType(): FileType =
    when (this.split(".").last()) {
        "mp4", "mkv" -> FileType.VIDEO
        "mpeg", "mp3", "wav" -> FileType.AUDIO
        "pdf" -> FileType.PDF
        "exe" -> FileType.EXE
        "txt" -> FileType.TXT
        else -> FileType.UNKNOWN
    }

private fun String.fileName(): String {
    val uri = Uri.parse(this)
    return uri.lastPathSegment ?: "unknown"
}

private val sampleUrls: List<String> = listOf(
    "https://www.example.com/The.Fall.Guy.2024.1080p.AMZN.WEBRip.1400MB.DD5.1.x264-GalaxyRG.mkv",
    "https://www.example.com/kanye_west_heartbreak.mp3",
    "https://www.example.com/certificate.pdf",
    "https://www.example.com/install_me.exe",
    "https://www.example.com/that report.txt",
)

private val sampleFileSizes: List<Double> = listOf(
    100.0,
    200.0,
    300.0,
    400.0,
    500.0,
)

enum class DownloadState {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
}

enum class FileType {
    VIDEO,
    AUDIO,
    PDF,
    EXE,
    TXT,
    UNKNOWN,
}