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
    var totalSize: Long,
    val currentSize: Long,
    val progress: Long,
    val state: DownloadState,
    val fileType: FileType,
    var mime: String,
    val localPath: String,
    val timeAdded: Long,
    val timeStarted: Long,
    val timeCompleted: Long,
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
            localPath = "unknown",
            timeAdded = 102120210212012,
            timeStarted = 102120210212012,
            timeCompleted = 1220227200,
        )

        val sample: DownloadTask
            get() {
                val url = sampleUrls.random()
                val totalSize = 123238833L
                val currentSize = totalSize - 123238
                return new(url).copy(
                    totalSize = totalSize,
                    currentSize = currentSize,
                    progress = 34,
                    state = DownloadState.entries.random(),
                    mime = "media/song"
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