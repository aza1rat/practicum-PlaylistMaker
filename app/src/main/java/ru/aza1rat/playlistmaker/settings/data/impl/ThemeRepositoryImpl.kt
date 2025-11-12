package ru.aza1rat.playlistmaker.settings.data.impl

import ru.aza1rat.playlistmaker.settings.domain.ThemeRepository
import ru.aza1rat.playlistmaker.settings.data.BaseThemeControl

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