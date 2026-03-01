package ru.aza1rat.playlistmaker.playlist.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.playlist.domain.api.CopyFileToStorageUseCase
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.aza1rat.playlistmaker.playlist.ui.model.CreatePlaylistState
import ru.aza1rat.playlistmaker.playlist.ui.api.InputPlaylistViewModel

class CreatePlaylistViewModel(
    private val copyFileToStorageUseCase: CopyFileToStorageUseCase,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel(), InputPlaylistViewModel {
    private val playlistState = MutableLiveData<CreatePlaylistState>()
    override fun observePlaylistState(): LiveData<CreatePlaylistState> = playlistState
    private var imageUri: Uri? = null

    override fun imageUriObtained(uri: Uri) {
        imageUri = uri
        playlistState.value = CreatePlaylistState.Content(uri)
    }

    fun createPlaylist(name: String, description: String) {
        var savedFileUri: Uri? = null
        if (name.isEmpty())
            return
        if (imageUri != null) {
            savedFileUri = copyFileToStorageUseCase.execute(imageUri!!) ?: return
        }
        viewModelScope.launch {
            playlistInteractor.addPlaylist(
                name, description.ifEmpty { null }, savedFileUri
            )
            playlistState.value = CreatePlaylistState.Created(name)
        }
    }
}