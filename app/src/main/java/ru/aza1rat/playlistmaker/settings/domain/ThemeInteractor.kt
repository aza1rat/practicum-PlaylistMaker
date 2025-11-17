package ru.aza1rat.playlistmaker.settings.domain

interface ThemeInteractor {
    fun isDarkTheme(): Boolean
    fun switchTheme()
}