package com.example.nasaspaceimages.data.local

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidImageDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun download(
        url: String,
        nasaId: String,
        title: String
    ): Long {
        val extension = Uri.parse(url)
            .lastPathSegment
            .orEmpty()
            .substringAfterLast('.', "jpg")
            .lowercase()

        val safeId = nasaId.replace(
            Regex("[^A-Za-z0-9_-]"),
            "_"
        )

        val fileName =
            "NASA_${safeId}_${System.currentTimeMillis()}.$extension"

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(title)
            .setDescription("Загрузка изображения NASA")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
        } else {
            request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
        }

        val manager = context.getSystemService(
            Context.DOWNLOAD_SERVICE
        ) as DownloadManager

        return manager.enqueue(request)
    }
}