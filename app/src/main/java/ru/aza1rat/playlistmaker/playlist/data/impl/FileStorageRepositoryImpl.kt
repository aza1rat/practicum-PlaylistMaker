package ru.aza1rat.playlistmaker.playlist.data.impl

import android.net.Uri
import ru.aza1rat.playlistmaker.playlist.data.FileStorageSaver
import ru.aza1rat.playlistmaker.playlist.domain.api.FileStorageRepository
import kotlin.io.path.fileVisitor

class FileStorageRepositoryImpl(private val fileStorageSaver: FileStorageSaver): FileStorageRepository {
    override fun copyFromFile(uri: Uri): Uri? {
        return fileStorageSaver.saveFile(uri)
    }
}