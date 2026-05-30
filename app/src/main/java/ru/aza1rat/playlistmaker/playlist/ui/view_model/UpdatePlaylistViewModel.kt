package ru.aza1rat.playlistmaker.playlist.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.playlist.domain.api.CopyFileToStorageUseCase
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist
import ru.aza1rat.playlistmaker.playlist.ui.model.CreatePlaylistState
import ru.aza1rat.playlistmaker.playlist.ui.api.InputPlaylistViewModel

class UpdatePlaylistViewModel(
    private val copyFileToStorageUseCase: CopyFileToStorageUseCase,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel(), InputPlaylistViewModel {
    private val playlistState = MutableLiveData<CreatePlaylistState>()
    override fun observePlaylistState(): LiveData<CreatePlaylistState> = playlistState
    private var imageUri: Uri? = null
    private var playlist: Playlist? = null

    override fun imageUriObtained(uri: Uri) {
        imageUri = uri
        playlistState.value = CreatePlaylistState.Content(uri)
    }

    fun updatePlaylist(name: String, description: String) {
        if (name.isEmpty() || playlist == null) return
        val playlist = playlist!!
        var finalImageUri = playlist.coverUri
        if (imageUri != null)
            finalImageUri = copyFileToStorageUseCase.execute(imageUri!!) ?: return
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(
                playlist.id,
                name,
                description.ifEmpty { null },
                finalImageUri
            )
            playlistState.value = CreatePlaylistState.Created(name)
        }
    }

    fun setCurrentPlaylist(playlist: Playlist) {
        this.playlist = playlist
    }
}