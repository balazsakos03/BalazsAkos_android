package com.example.progresshabitplanner.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageSaver {
    fun saveImageFromUri(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            val file = File(context.filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            file.absolutePath   // << ezt fogjuk elmenteni
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}