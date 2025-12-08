package ru.aza1rat.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.aza1rat.playlistmaker.settings.domain.ThemeInteractor
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor
import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val darkThemeEnabled = MutableLiveData<Boolean>()
    fun observeDarkThemeEnabled(): LiveData<Boolean> = darkThemeEnabled

    init {
        darkThemeEnabled.value = themeInteractor.isDarkTheme()
    }

    fun shareApp(link: String) {
        sharingInteractor.shareApp(link)
    }

    fun openSupport(email: String, subject: String, text: String) {
        sharingInteractor.openSupport(EmailData(
            email, subject, text
        ))
    }
    fun openTerms(link: String) {
        sharingInteractor.openTerms(link)
    }
    fun switchTheme(switchedToDark: Boolean) {
        if (darkThemeEnabled.value != switchedToDark) {
            themeInteractor.switchTheme()
            darkThemeEnabled.value = themeInteractor.isDarkTheme()
        }

    }
}