package ru.aza1rat.playlistmaker.playlist.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AppFileStorageSaver(private val context: Context) : FileStorageSaver {
    override fun saveFile(fromFile: Uri): Uri? {
        try {
            val inputStream = context.contentResolver.openInputStream(fromFile)
            val dir = File(context.filesDir, COVERS_DIRECTORY)
            dir.mkdir()
            val postfix = dir.listFiles()?.size ?: 0
            val outputFile = File(dir, "${FILE_NAME_PREFIX}${postfix}${FILE_EXTENSION}")
            val outputStream = FileOutputStream(outputFile)
            BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, outputStream)
            inputStream?.close()
            outputStream.close()
            return outputFile.toUri()
        } catch (e: Exception) {
            return null
        }
    }

    companion object {
        private const val COVERS_DIRECTORY = "playlists"
        private const val FILE_NAME_PREFIX = "cover"
        private const val FILE_EXTENSION = ".jpg"
        private const val COMPRESS_QUALITY = 70
    }
}