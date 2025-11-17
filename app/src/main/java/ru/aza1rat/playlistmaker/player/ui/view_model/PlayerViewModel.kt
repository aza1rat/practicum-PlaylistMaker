package ru.aza1rat.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.creator.Creator
import ru.aza1rat.playlistmaker.player.domain.api.PlayerInteractor
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.player.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor, private val placeholderProgress: String) : ViewModel() {
    private val playerState = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private val progressPlaying = MutableLiveData<String>()
    fun observeProgressPlaying(): LiveData<String> = progressPlaying

    private val progressFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())
    private val progressCheckTask = object : Runnable {
        override fun run() {
            progressPlaying.value = progressFormatter.format(playerInteractor.getCurrentPosition())
            if (playerInteractor.getCurrentState() == PlayerState.PLAYING)
                handler.postDelayed(this, PROGRESS_CHECK_DELAY)
        }
    }

    init {
        playerInteractor.setOnPlayerStateChangeListener(object : PlayerRepository.PlayerStateChangeListener {
            override fun onPlayerStateChanged(state: PlayerState) {
                when (state) {
                    PlayerState.PREPARED -> {
                        playerState.value = PlayerState.PREPARED
                    }
                    PlayerState.COMPLETED -> {
                        handler.removeCallbacks(progressCheckTask)
                        playerState.value = PlayerState.COMPLETED
                        progressPlaying.value = placeholderProgress
                    }
                    PlayerState.PLAYING -> {
                        handler.postDelayed(progressCheckTask, PROGRESS_CHECK_DELAY)
                        playerState.value = PlayerState.PLAYING
                    }
                    PlayerState.PAUSED -> {
                        handler.removeCallbacks(progressCheckTask)
                        playerState.value = PlayerState.PAUSED
                    }
                    PlayerState.DEFAULT -> {
                        playerState.value = PlayerState.DEFAULT
                    }
                }
            }
        })
    }

    fun preparePlayer(trackUrl: String) {
        if (playerInteractor.getCurrentState() == PlayerState.DEFAULT)
            playerInteractor.preparePlayer(trackUrl)
    }

    fun playOrPause() {
        if (playerInteractor.getCurrentState() == PlayerState.DEFAULT) return
        if (playerInteractor.getCurrentState() == PlayerState.PLAYING)
            pause()
        else
            play()
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

    companion object {
        private const val PROGRESS_CHECK_DELAY = 400L

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val placeholder = (this[APPLICATION_KEY] as Application).getString(R.string.placeholder_progress_playing)
                PlayerViewModel(Creator.providePlayerInteractor(),placeholder)
            }
        }
    }
}