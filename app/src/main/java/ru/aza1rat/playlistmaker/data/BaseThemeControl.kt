package ru.aza1rat.playlistmaker.data

interface BaseThemeControl {
    fun savedThemeIsDark(): Boolean
    fun setDarkTheme(darkTheme: Boolean)
    fun saveTheme()
}