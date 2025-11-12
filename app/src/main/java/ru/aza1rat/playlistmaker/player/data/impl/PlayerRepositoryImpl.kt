package ru.aza1rat.playlistmaker.player.data.impl

import android.media.MediaPlayer
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.player.domain.model.PlayerState

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private var onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener? = null
): PlayerRepository {
    private var currentState: PlayerState = PlayerState.DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        changeState(PlayerState.PLAYING)
    }

    override fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            changeState(PlayerState.PAUSED)
        }
    }

    override fun preparePlayer(trackUrl: String) {
        mediaPlayer.setDataSource(trackUrl)
        changeState(PlayerState.DEFAULT)
        mediaPlayer.setOnPreparedListener {
            changeState(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            changeState(PlayerState.COMPLETED)
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

    private fun changeState(state: PlayerState) {
        currentState = state
        this.onPlayerStateChangeListener?.onPlayerStateChanged(state)
    }
}