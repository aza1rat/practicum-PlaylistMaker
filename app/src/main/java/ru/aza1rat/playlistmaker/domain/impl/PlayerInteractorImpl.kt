package ru.aza1rat.playlistmaker.domain.impl

import ru.aza1rat.playlistmaker.domain.api.interactor.PlayerInteractor
import ru.aza1rat.playlistmaker.domain.api.repository.PlayerRepository
import ru.aza1rat.playlistmaker.domain.api.repository.PlayerRepository.PlayerStateChangeListener
import ru.aza1rat.playlistmaker.domain.model.PlayerState

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun preparePlayer(trackUrl: String) {
        repository.preparePlayer(trackUrl)
    }

    override fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerStateChangeListener) {
        repository.setOnPlayerStateChangeListener(onPlayerStateChangeListener)
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun release() {
        repository.release()
    }

    override fun getCurrentState(): PlayerState {
        return repository.getCurrentState()
    }
}