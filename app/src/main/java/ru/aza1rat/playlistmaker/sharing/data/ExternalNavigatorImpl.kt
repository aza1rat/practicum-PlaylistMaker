package ru.aza1rat.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ru.aza1rat.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        Intent(Intent.ACTION_SEND).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setType("plain/text")
            putExtra(Intent.EXTRA_TEXT, link)
            context.startActivity(this)
        }
    }

    override fun openLink(link: String) {
        Intent(Intent.ACTION_VIEW).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setData(link.toUri())
            context.startActivity(this)
        }
    }

    override fun openEmail(email: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setData("mailto:".toUri())
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
            putExtra(Intent.EXTRA_SUBJECT, email.subject)
            putExtra(Intent.EXTRA_TEXT, email.text)
            context.startActivity(this)
        }
    }
}