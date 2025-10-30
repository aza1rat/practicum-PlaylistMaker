package ru.aza1rat.playlistmaker.domain.impl

import ru.aza1rat.playlistmaker.domain.api.interactor.ThemeInteractor
import ru.aza1rat.playlistmaker.domain.api.repository.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository): ThemeInteractor {
    override fun isDarkTheme(): Boolean {
        return repository.savedThemeIsDark()
    }

    override fun switchTheme() {
        repository.setDarkTheme(!repository.savedThemeIsDark())
        repository.saveTheme()
    }
}