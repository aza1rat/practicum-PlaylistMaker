package ru.aza1rat.playlistmaker.player.ui.model

sealed class PlayerState(var trackIsFavourite: Boolean) {
    data class NotReady(val favourite: Boolean) : PlayerState(favourite)
    data class Prepared(val favourite: Boolean) : PlayerState(favourite)
    data class Completed(val favourite: Boolean) : PlayerState(favourite)
    data class Playing(val progress: String, val favourite: Boolean) : PlayerState(favourite)
    data class Paused(val progress: String, val favourite: Boolean) : PlayerState(favourite)
}