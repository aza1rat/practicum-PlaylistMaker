package ru.aza1rat.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val handler = Handler(Looper.getMainLooper())
    private val progressCheckTask = object : Runnable {
        override fun run() {
            progressPlaying = formatProgress(playerInteractor.getCurrentPosition())
            val state = playerInteractor.getCurrentState()
            if (state == MediaPlayerState.PLAYING) {
                playerState.value = PlayerState.Playing(progressPlaying)
                handler.postDelayed(this, PROGRESS_CHECK_DELAY)
            }
            if (state == MediaPlayerState.PAUSED)
                playerState.value = PlayerState.Paused(progressPlaying)
        }
    }

    init {
        playerInteractor.setOnPlayerStateChangeListener(object : PlayerRepository.PlayerStateChangeListener {
            override fun onPlayerStateChanged(state: MediaPlayerState) {
                when (state) {
                    MediaPlayerState.PREPARED -> {
                        playerState.value = PlayerState.Prepared
                    }
                    MediaPlayerState.COMPLETED -> {
                        handler.removeCallbacks(progressCheckTask)
                        progressPlaying = formatProgress(0)
                        playerState.value = PlayerState.Completed
                    }
                    MediaPlayerState.PLAYING -> {
                        handler.postDelayed(progressCheckTask, PROGRESS_CHECK_DELAY)
                        playerState.value = PlayerState.Playing(progressPlaying)
                    }
                    MediaPlayerState.PAUSED -> {
                        handler.removeCallbacks(progressCheckTask)
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
        handler.removeCallbacks(progressCheckTask)
        super.onCleared()
    }

    private fun formatProgress(progress: Int): String = progressFormatter.format(progress)

    companion object {
        private const val PROGRESS_CHECK_DELAY = 400L
    }
}