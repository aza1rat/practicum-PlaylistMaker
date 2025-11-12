package ru.aza1rat.playlistmaker.settings.domain

interface ThemeRepository {
    fun savedThemeIsDark(): Boolean
    fun setDarkTheme(darkTheme: Boolean)
    fun saveTheme()
}