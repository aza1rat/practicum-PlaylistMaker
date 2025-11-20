package ru.aza1rat.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.aza1rat.playlistmaker.settings.domain.ThemeInteractor
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val darkThemeEnabled = MutableLiveData<Boolean>()
    fun observeDarkThemeEnabled(): LiveData<Boolean> = darkThemeEnabled

    init {
        darkThemeEnabled.value = themeInteractor.isDarkTheme()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
    fun openTerms() {
        sharingInteractor.openTerms()
    }
    fun switchTheme(switchedToDark: Boolean) {
        if (darkThemeEnabled.value != switchedToDark) {
            themeInteractor.switchTheme()
            darkThemeEnabled.value = themeInteractor.isDarkTheme()
        }

    }
}