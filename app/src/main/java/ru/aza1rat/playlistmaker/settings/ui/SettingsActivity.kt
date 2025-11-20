package ru.aza1rat.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.databinding.ActivitySettingsBinding
import ru.aza1rat.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.share.setOnClickListener {
            settingsViewModel.shareApp()
        }
        binding.messageSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }
        binding.userAgreement.setOnClickListener {
            settingsViewModel.openTerms()
        }

        settingsViewModel.observeDarkThemeEnabled().observe(this) {
            binding.themeSwitcher.isChecked = it
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }
        binding.back.setOnClickListener { finish() }
    }
}