package ru.aza1rat.playlistmaker.playlist.domain.api

import android.net.Uri

interface FileStorageRepository {
    fun copyFromFile(uri: Uri): Uri?
}