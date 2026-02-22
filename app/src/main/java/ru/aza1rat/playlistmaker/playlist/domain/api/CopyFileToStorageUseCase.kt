package ru.aza1rat.playlistmaker.playlist.domain.api

import android.net.Uri

interface CopyFileToStorageUseCase {
    fun execute(uri: Uri): Uri?
}