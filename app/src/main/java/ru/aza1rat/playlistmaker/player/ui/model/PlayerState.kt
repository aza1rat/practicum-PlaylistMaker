package ru.aza1rat.playlistmaker.player.ui.model

sealed interface PlayerState {
    object NotReady : PlayerState
    object Prepared : PlayerState
    object Completed : PlayerState
    data class Playing(val progress: String) : PlayerState
    data class Paused(val progress: String) : PlayerState
}