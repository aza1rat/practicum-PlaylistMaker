package ru.aza1rat.playlistmaker.domain.api.repository

import ru.aza1rat.playlistmaker.domain.model.PlayerState

interface PlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl: String)
    fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerStateChangeListener)
    fun getCurrentPosition(): Int
    fun release()
    fun getCurrentState(): PlayerState

    interface PlayerStateChangeListener {
        fun onPlayerStateChanged(state: PlayerState)
    }
}