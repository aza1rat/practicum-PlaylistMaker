package ru.aza1rat.playlistmaker.player.data.impl

import android.media.MediaPlayer
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.player.domain.model.MediaPlayerState

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val trackDao: TrackDao,
    private var onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener? = null
): PlayerRepository {
    private var currentState: MediaPlayerState = MediaPlayerState.DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        changeState(MediaPlayerState.PLAYING)
    }

    override fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            changeState(MediaPlayerState.PAUSED)
        }
    }

    override fun preparePlayer(trackUrl: String) {
        mediaPlayer.setDataSource(trackUrl)
        changeState(MediaPlayerState.DEFAULT)
        mediaPlayer.setOnPreparedListener {
            changeState(MediaPlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            changeState(MediaPlayerState.COMPLETED)
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

    override fun getCurrentState(): MediaPlayerState {
        return currentState
    }

    override suspend fun trackIsFavourite(trackId: Int): Boolean {
        return trackDao.getTracksCountById(trackId) > 0
    }

    private fun changeState(state: MediaPlayerState) {
        currentState = state
        this.onPlayerStateChangeListener?.onPlayerStateChanged(state)
    }
}