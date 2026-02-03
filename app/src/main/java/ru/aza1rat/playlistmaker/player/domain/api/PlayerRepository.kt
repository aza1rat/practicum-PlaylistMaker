package ru.aza1rat.playlistmaker.player.domain.api

import ru.aza1rat.playlistmaker.player.domain.model.MediaPlayerState

interface PlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl: String)
    fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerStateChangeListener)
    fun getCurrentPosition(): Int
    fun release()
    fun getCurrentState(): MediaPlayerState
    suspend fun trackIsFavourite(trackId: Int): Boolean

    interface PlayerStateChangeListener {
        fun onPlayerStateChanged(state: MediaPlayerState)
    }
}