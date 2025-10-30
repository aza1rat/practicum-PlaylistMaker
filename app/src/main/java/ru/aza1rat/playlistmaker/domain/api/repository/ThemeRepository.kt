package ru.aza1rat.playlistmaker.domain.api.repository

interface ThemeRepository {
    fun savedThemeIsDark(): Boolean
    fun setDarkTheme(darkTheme: Boolean)
    fun saveTheme()
}