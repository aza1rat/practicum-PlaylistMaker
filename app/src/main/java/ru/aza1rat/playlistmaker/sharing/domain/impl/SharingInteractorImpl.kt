package ru.aza1rat.playlistmaker.sharing.domain.impl

import ru.aza1rat.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingResourceRepository
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingResourceRepository: SharingResourceRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(sharingResourceRepository.getSharingLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(sharingResourceRepository.getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(sharingResourceRepository.getEmailSupport())
    }
}