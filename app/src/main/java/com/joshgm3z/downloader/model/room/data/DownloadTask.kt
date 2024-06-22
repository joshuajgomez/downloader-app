package com.joshgm3z.downloader.model.room.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity
data class DownloadTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val url: String,
    val filename: String,
    val totalSize: Long,
    val currentSize: Long,
    val progress: Long,
    val state: DownloadState,
    val fileType: FileType,
    val mime: String,
    val localPath: String,
) {
    companion object {
        fun new(url: String): DownloadTask = DownloadTask(
            id = 0,
            url = url,
            filename = url.fileName(),
            totalSize = 0,
            currentSize = 0,
            progress = 0,
            state = DownloadState.PENDING,
            fileType = url.fileType(),
            mime = "unknown",
            localPath = "unknown"
        )

        val sample: DownloadTask
            get() {
                val url = sampleUrls.random()
                val totalSize = sampleFileSizes.random()
                val currentSize = totalSize - Random.nextLong(totalSize)
                return new(url).copy(
                    totalSize = totalSize,
                    currentSize = currentSize,
                    progress = (currentSize / totalSize) * 100,
                    state = DownloadState.entries.random(),
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

    override fun toString(): String {
        return "DownloadTask(id=$id, " +
                "url='$url', " +
                "filename='$filename', " +
                "totalSize=$totalSize, " +
                "currentSize=$currentSize, " +
                "progress=$progress, " +
                "state=$state, " +
                "fileType=$fileType, " +
                "mime=$mime, " +
                "localPath=${localPath}, " +
                ")"
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

private val sampleFileSizes: List<Long> = listOf(
    100,
    200,
    300,
    400,
    500,
)

enum class DownloadState {
    PENDING,
    RUNNING,
    PAUSED,
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