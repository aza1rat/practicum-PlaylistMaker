package ru.aza1rat.playlistmaker.playlist.data

import android.net.Uri

interface FileStorageSaver {
    fun saveFile(fromFile: Uri): Uri?
}