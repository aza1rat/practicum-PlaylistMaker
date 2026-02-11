package ru.aza1rat.playlistmaker.player.domain.impl

import ru.aza1rat.playlistmaker.player.domain.model.MediaPlayerState
import ru.aza1rat.playlistmaker.player.domain.api.PlayerInteractor
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository

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

    override fun setOnPlayerStateChangeListener(onPlayerStateChangeListener: PlayerRepository.PlayerStateChangeListener) {
        repository.setOnPlayerStateChangeListener(onPlayerStateChangeListener)
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun release() {
        repository.release()
    }

    override fun getCurrentState(): MediaPlayerState {
        return repository.getCurrentState()
    }

    override suspend fun trackIsFavourite(trackId: Int): Boolean {
        return repository.trackIsFavourite(trackId)
    }
}