package ru.aza1rat.playlistmaker.playlist.ui.api

import android.net.Uri
import androidx.lifecycle.LiveData
import ru.aza1rat.playlistmaker.playlist.ui.model.CreatePlaylistState

interface InputPlaylistViewModel {
    fun observePlaylistState(): LiveData<CreatePlaylistState>
    fun imageUriObtained(uri: Uri)

}