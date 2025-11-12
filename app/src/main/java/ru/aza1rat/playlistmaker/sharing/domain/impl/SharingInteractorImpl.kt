package ru.aza1rat.playlistmaker.sharing.domain.impl

import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData
import ru.aza1rat.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingLink: String,
    private val termsLink: String,
    private val email: EmailData
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(sharingLink)
    }

    override fun openTerms() {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport() {
        externalNavigator.openEmail(email)
    }
}