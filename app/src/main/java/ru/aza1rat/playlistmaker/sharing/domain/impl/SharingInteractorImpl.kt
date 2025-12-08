package ru.aza1rat.playlistmaker.sharing.domain.impl

import ru.aza1rat.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor
import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(link)
    }

    override fun openSupport(email: EmailData) {
        externalNavigator.openEmail(email)
    }
}