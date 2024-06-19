package com.joshgm3z.downloader.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.joshgm3z.downloader.model.room.data.DownloadTask
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject


class FileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun openFile(downloadTask: DownloadTask) {
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ),
                downloadTask.filename
            )
            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                file
            )
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.startActivity(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMimeType(url: String): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        val type = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension) ?: "unknown"
        return type
    }
}