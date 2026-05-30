package ru.aza1rat.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.media_library.domain.api.PlaylistTracksInteractor
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.aza1rat.playlistmaker.playlist.domain.model.PlaylistTracks
import ru.aza1rat.playlistmaker.playlist.ui.model.PlaylistDialogEvent
import ru.aza1rat.playlistmaker.playlist.ui.model.PlaylistState
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor
import ru.aza1rat.playlistmaker.util.ui.SingleLiveEvent

class PlaylistViewModel(
    private val playlistTracksInteractor: PlaylistTracksInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private var playlistTracks: PlaylistTracks? = null
    private val playlistState = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState
    private val playlistDialogEvent = SingleLiveEvent<PlaylistDialogEvent>()
    fun observePlaylistDialogEvent(): LiveData<PlaylistDialogEvent> = playlistDialogEvent

    fun getPlaylistTracks(playlistId: Int) {
        viewModelScope.launch {
            with(playlistTracksInteractor.getPlaylistTracks(playlistId)) {
                playlistTracks = this
                playlistState.value = PlaylistState.Content(this)
            }
        }
    }

    fun deleteTrackFromPlaylist(trackId: Int) {
        playlistTracks?.let { currentPlaylistTracks ->
            val trackIndex = currentPlaylistTracks.tracks.indexOfFirst { it.trackId == trackId }
            viewModelScope.launch {
                playlistTracksInteractor.deleteTrackFromPlaylist(
                    currentPlaylistTracks.playlist.id,
                    trackId
                )
                with (playlistTracksInteractor.getPlaylistTracks(currentPlaylistTracks.playlist.id)) {
                    playlistTracks = this
                    playlistState.value = PlaylistState.TrackRemoved(trackIndex, this)
                }
            }
        }
    }

    fun onDeletePlaylistClick() {
        playlistTracks?.let {
            playlistDialogEvent.value = PlaylistDialogEvent.DeletePlaylistDialog
        }
    }

    fun sharePlaylist(message: String) {
        sharingInteractor.shareApp(message)
    }

    fun deletePlaylist() {
        playlistTracks?.let {
            viewModelScope.launch {
                playlistInteractor.deletePlaylist(it.playlist.id)
                playlistState.value = PlaylistState.Deleted
            }
        }
    }

    fun onShareClick() {
        playlistTracks?.let { playlistTracks ->
            if (playlistTracks.tracks.isEmpty())
                playlistDialogEvent.value = PlaylistDialogEvent.UnableToShare
            else {
                playlistDialogEvent.value = PlaylistDialogEvent.ShareTracksMessage(
                    tracksCount = playlistTracks.tracks.size,
                    message = { tracksCountStr ->
                        var tracksStr = ""
                        for (i in playlistTracks.tracks.indices) {
                            val track = playlistTracks.tracks[i]
                            tracksStr += "${i + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})\n"
                        }
                        "${playlistTracks.playlist.name}\n${playlistTracks.playlist.description?.let { "$it " } ?: ""}${tracksCountStr}\n${tracksStr}"
                    }
                )
            }
        }
    }
}