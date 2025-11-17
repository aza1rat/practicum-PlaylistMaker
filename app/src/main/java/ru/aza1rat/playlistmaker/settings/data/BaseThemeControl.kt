package ru.aza1rat.playlistmaker.settings.data

interface BaseThemeControl {
    fun savedThemeIsDark(): Boolean
    fun setDarkTheme(darkTheme: Boolean)
    fun saveTheme()
}