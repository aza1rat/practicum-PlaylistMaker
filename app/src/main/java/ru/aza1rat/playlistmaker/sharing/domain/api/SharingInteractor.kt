package ru.aza1rat.playlistmaker.sharing.domain.api

import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(link: String)
    fun openTerms(link: String)
    fun openSupport(email: EmailData)
}