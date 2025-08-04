package ru.aza1rat.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val shareTextView = findViewById<MaterialButton>(R.id.share)
        shareTextView.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                setType("plain/text")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
                startActivity(this)
            }
        }
        val messageSupportTextView = findViewById<MaterialButton>(R.id.messageSupport)
        messageSupportTextView.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                setData("mailto:".toUri())
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.author_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_support_title))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_support_text))
                startActivity(this)
            }
        }
        val userAgreementTextView = findViewById<MaterialButton>(R.id.userAgreement)
        userAgreementTextView.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                setData(getString(R.string.link_user_agreement).toUri())
                startActivity(this)
            }
        }
        val backButton = findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener { finish() }
    }
}