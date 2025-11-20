package ru.aza1rat.playlistmaker.player.domain.api

import ru.aza1rat.playlistmaker.player.domain.model.MediaPlayerState

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl: String)
    fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener)
    fun getCurrentPosition(): Int
    fun release()
    fun getCurrentState(): MediaPlayerState
}