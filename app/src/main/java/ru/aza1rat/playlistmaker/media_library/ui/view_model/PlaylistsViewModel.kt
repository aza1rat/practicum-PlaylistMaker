package ru.aza1rat.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.media_library.domain.api.PlaylistTracksInteractor
import ru.aza1rat.playlistmaker.media_library.ui.model.PlaylistState

class PlaylistsViewModel(private val playlistTracksInteractor: PlaylistTracksInteractor) : ViewModel() {
    private val playlistState = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState

    fun getPlaylists() {
        viewModelScope.launch {
            playlistTracksInteractor.listPlaylistWithTracksCount().collect { playlists ->
                if (playlists.isEmpty())
                    playlistState.value = PlaylistState.Empty
                else {
                    playlistState.value = PlaylistState.Content(playlists)
                }
            }
        }
    }
}