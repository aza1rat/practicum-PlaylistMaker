package ru.aza1rat.playlistmaker.data.impl

import ru.aza1rat.playlistmaker.data.BaseThemeControl
import ru.aza1rat.playlistmaker.domain.api.repository.ThemeRepository

class ThemeRepositoryImpl(private val themeControl: BaseThemeControl): ThemeRepository {
    override fun savedThemeIsDark(): Boolean {
        return themeControl.savedThemeIsDark()
    }

    override fun setDarkTheme(darkTheme: Boolean) {
        themeControl.setDarkTheme(darkTheme)
    }

    override fun saveTheme() {
        themeControl.saveTheme()
    }
}