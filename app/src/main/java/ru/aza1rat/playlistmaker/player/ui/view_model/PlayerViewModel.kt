package ru.aza1rat.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesInteractor
import ru.aza1rat.playlistmaker.player.domain.api.PlayerInteractor
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.player.domain.model.MediaPlayerState
import ru.aza1rat.playlistmaker.player.ui.model.PlayerState
import ru.aza1rat.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {
    private val playerState = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private val progressFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var progressPlaying: String = formatProgress(0)
    private var progressCheckJob: Job? = null
    private var isFavourite: Boolean = false

    init {
        playerInteractor.setOnPlayerStateChangeListener(object :
            PlayerRepository.PlayerStateChangeListener {
            override fun onPlayerStateChanged(state: MediaPlayerState) {
                when (state) {
                    MediaPlayerState.PREPARED -> {
                        playerState.value = PlayerState.Prepared(isFavourite)
                    }

                    MediaPlayerState.COMPLETED -> {
                        progressCheckJob?.cancel()
                        progressPlaying = formatProgress(0)
                        playerState.value = PlayerState.Completed(isFavourite)
                    }

                    MediaPlayerState.PLAYING -> {
                        startProgressCheckJob()
                    }

                    MediaPlayerState.PAUSED -> {
                        progressCheckJob?.cancel()
                        playerState.value = PlayerState.Paused(progressPlaying, isFavourite)
                    }

                    MediaPlayerState.DEFAULT -> {
                        playerState.value = PlayerState.NotReady(isFavourite)
                    }
                }
            }
        })
    }

    fun preparePlayer(trackUrl: String, trackId: Int) {
        if (playerInteractor.getCurrentState() == MediaPlayerState.DEFAULT) {
            playerInteractor.preparePlayer(trackUrl)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    isFavourite = playerInteractor.trackIsFavourite(trackId)
                }
                notifyTrackIsFavouriteChanged()
            }
        }
    }

    fun togglePlay() {
        when (playerInteractor.getCurrentState()) {
            MediaPlayerState.DEFAULT -> return
            MediaPlayerState.PLAYING -> pause()
            else -> play()
        }
    }

    fun toggleFavourite(track: Track) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (isFavourite) {
                    true -> favouritesInteractor.delete(track.trackId)
                    false -> favouritesInteractor.add(track)
                }
            }
            isFavourite = !isFavourite
            notifyTrackIsFavouriteChanged()
        }
    }


    fun notifyTrackIsFavouriteChanged() {
        if (playerState.value != null) {
            (playerState.value as PlayerState).apply {
                this.trackIsFavourite = isFavourite
                playerState.value = this
            }
        }
    }

    fun play() {
        playerInteractor.startPlayer()
    }

    fun pause() {
        playerInteractor.pausePlayer()
    }

    override fun onCleared() {
        playerInteractor.release()
        super.onCleared()
    }

    private fun formatProgress(progress: Int): String = progressFormatter.format(progress)

    private fun startProgressCheckJob() {
        progressCheckJob = viewModelScope.launch {
            while (playerInteractor.getCurrentState() == MediaPlayerState.PLAYING) {
                progressPlaying = formatProgress(playerInteractor.getCurrentPosition())
                playerState.value = PlayerState.Playing(progressPlaying, isFavourite)
                delay(PROGRESS_CHECK_DELAY)
            }
        }
    }

    companion object {
        private const val PROGRESS_CHECK_DELAY = 300L
    }
}