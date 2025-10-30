package ru.aza1rat.playlistmaker.data.impl

import android.media.MediaPlayer
import ru.aza1rat.playlistmaker.domain.api.repository.PlayerRepository
import ru.aza1rat.playlistmaker.domain.model.PlayerState

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private var onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener? = null
): PlayerRepository {
    private var currentState: PlayerState = PlayerState.DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        currentState = PlayerState.PLAYING
        onPlayerStateChangeListener?.onPlayerStateChanged(PlayerState.PLAYING)
    }

    override fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            currentState = PlayerState.PAUSED
            onPlayerStateChangeListener?.onPlayerStateChanged(PlayerState.PAUSED)
        }
    }

    override fun preparePlayer(trackUrl: String) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.setOnPreparedListener {
            currentState = PlayerState.PREPARED
            onPlayerStateChangeListener?.onPlayerStateChanged(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            currentState = PlayerState.COMPLETED
            onPlayerStateChangeListener?.onPlayerStateChanged(PlayerState.COMPLETED)
        }
        mediaPlayer.prepareAsync()
    }

    override fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener) {
        this.onPlayerStateChangeListener = onPlayerStateChangeListener
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentState(): PlayerState {
        return currentState
    }
}