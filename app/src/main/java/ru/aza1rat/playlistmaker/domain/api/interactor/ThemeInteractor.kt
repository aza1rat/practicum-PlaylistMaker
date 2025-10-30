package ru.aza1rat.playlistmaker.domain.api.interactor

interface ThemeInteractor {
    fun isDarkTheme(): Boolean
    fun switchTheme()
}