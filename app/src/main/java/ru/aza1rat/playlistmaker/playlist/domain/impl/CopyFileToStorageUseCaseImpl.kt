package ru.aza1rat.playlistmaker.playlist.domain.impl

import android.net.Uri
import ru.aza1rat.playlistmaker.playlist.domain.api.CopyFileToStorageUseCase
import ru.aza1rat.playlistmaker.playlist.domain.api.FileStorageRepository

class CopyFileToStorageUseCaseImpl(private val repository: FileStorageRepository) : CopyFileToStorageUseCase {
    override fun execute(uri: Uri): Uri? {
        return repository.copyFromFile(uri)
    }
}