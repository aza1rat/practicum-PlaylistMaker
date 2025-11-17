package ru.aza1rat.playlistmaker.sharing.domain.api

import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(email: EmailData)
}