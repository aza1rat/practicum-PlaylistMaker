package ru.aza1rat.playlistmaker.sharing.domain.api

import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

interface SharingResourceRepository {
    fun getSharingLink(): String
    fun getTermsLink(): String
    fun getEmailSupport(): EmailData
}