package ru.aza1rat.playlistmaker.sharing.data

import android.content.Context
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingResourceRepository
import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

class SharingResourceRepositoryImpl(private val context: Context): SharingResourceRepository
{
    override fun getSharingLink(): String {
        return context.getString(R.string.share_app_message)
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.link_user_agreement)
    }

    override fun getEmailSupport(): EmailData {
        return EmailData(
            context.getString(R.string.author_email),
            context.getString(R.string.email_support_title),
            context.getString(R.string.email_support_text)
        )
    }
}