package ru.aza1rat.playlistmaker.domain.api.interactor

import ru.aza1rat.playlistmaker.domain.api.repository.PlayerRepository
import ru.aza1rat.playlistmaker.domain.model.PlayerState

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl: String)
    fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener)
    fun getCurrentPosition(): Int
    fun release()
    fun getCurrentState(): PlayerState
}