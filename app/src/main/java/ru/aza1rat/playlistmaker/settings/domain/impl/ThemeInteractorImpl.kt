package ru.aza1rat.playlistmaker.settings.domain.impl

import ru.aza1rat.playlistmaker.settings.domain.ThemeInteractor
import ru.aza1rat.playlistmaker.settings.domain.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository): ThemeInteractor {
    override fun isDarkTheme(): Boolean {
        return repository.savedThemeIsDark()
    }

    override fun switchTheme() {
        repository.setDarkTheme(!repository.savedThemeIsDark())
        repository.saveTheme()
    }
}