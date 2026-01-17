package ru.aza1rat.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentSettingsBinding
import ru.aza1rat.playlistmaker.settings.ui.view_model.SettingsViewModel
import kotlin.getValue

class SettingsFragment: Fragment() {
    private val settingsViewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.share.setOnClickListener {
            settingsViewModel.shareApp(
                getString(R.string.share_app_message)
            )
        }
        binding.messageSupport.setOnClickListener {
            settingsViewModel.openSupport(
                getString(R.string.author_email),
                getString(R.string.email_support_title),
                getString(R.string.email_support_text)
            )
        }
        binding.userAgreement.setOnClickListener {
            settingsViewModel.openTerms(
                getString(R.string.link_user_agreement)
            )
        }
        settingsViewModel.observeDarkThemeEnabled().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}