package ru.aza1rat.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.player.domain.api.PlayerInteractor
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.player.domain.model.MediaPlayerState
import ru.aza1rat.playlistmaker.player.ui.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val playerState = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private val progressFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var progressPlaying: String = formatProgress(0)
    private var progressCheckJob: Job? = null

    init {
        playerInteractor.setOnPlayerStateChangeListener(object : PlayerRepository.PlayerStateChangeListener {
            override fun onPlayerStateChanged(state: MediaPlayerState) {
                when (state) {
                    MediaPlayerState.PREPARED -> {
                        playerState.value = PlayerState.Prepared
                    }
                    MediaPlayerState.COMPLETED -> {
                        progressCheckJob?.cancel()
                        progressPlaying = formatProgress(0)
                        playerState.value = PlayerState.Completed
                    }
                    MediaPlayerState.PLAYING -> {
                        startProgressCheckJob()
                    }
                    MediaPlayerState.PAUSED -> {
                        progressCheckJob?.cancel()
                        playerState.value = PlayerState.Paused(progressPlaying)
                    }
                    MediaPlayerState.DEFAULT -> {
                        playerState.value = PlayerState.NotReady
                    }
                }
            }
        })
    }

    fun preparePlayer(trackUrl: String) {
        if (playerInteractor.getCurrentState() == MediaPlayerState.DEFAULT)
            playerInteractor.preparePlayer(trackUrl)
    }

    fun playOrPause() {
        when(playerInteractor.getCurrentState()) {
            MediaPlayerState.DEFAULT -> return
            MediaPlayerState.PLAYING -> pause()
            else -> play()
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
            while(playerInteractor.getCurrentState() == MediaPlayerState.PLAYING) {
                progressPlaying = formatProgress(playerInteractor.getCurrentPosition())
                playerState.value = PlayerState.Playing(progressPlaying)
                delay(PROGRESS_CHECK_DELAY)
            }
        }
    }

    companion object {
        private const val PROGRESS_CHECK_DELAY = 300L
    }
}