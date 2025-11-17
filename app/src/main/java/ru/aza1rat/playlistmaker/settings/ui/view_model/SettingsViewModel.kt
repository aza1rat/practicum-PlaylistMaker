package ru.aza1rat.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.aza1rat.playlistmaker.App
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.creator.Creator
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

    companion object {
        fun getFactory() : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App
                val sharingLink = app.getString(R.string.share_app_message)
                val termsLink = app.getString(R.string.link_user_agreement)
                val email = app.getString(R.string.author_email)
                val subject = app.getString(R.string.email_support_title)
                val text = app.getString(R.string.email_support_text)
                SettingsViewModel(
                    Creator.provideThemeInteractor(app.themeControl),
                    Creator.provideSharingInteractor(
                        sharingLink, termsLink,
                        Creator.getSupportEmail(email, subject, text)
                    )
                )
            }
        }
    }
}