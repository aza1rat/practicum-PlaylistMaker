package ru.aza1rat.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class ThemeControl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
): BaseThemeControl {
    private var darkTheme: Boolean = false
    init {
        darkTheme = loadTheme()
    }


    override fun savedThemeIsDark(): Boolean {
        return darkTheme
    }

    override fun setDarkTheme(darkTheme: Boolean) {
            this.darkTheme = darkTheme
            AppCompatDelegate.setDefaultNightMode(
                if (darkTheme)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
    }

    override fun saveTheme() {
        sharedPreferences.edit { putBoolean(CURRENT_THEME_KEY, darkTheme) }
    }

    private fun loadTheme(): Boolean {
        val isDark = sharedPreferences.getBoolean(CURRENT_THEME_KEY, systemThemeIsDark())
        return isDark
    }

    private fun systemThemeIsDark(): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }

            else -> false
        }
    }

    companion object {
        const val CURRENT_THEME_KEY = "current_theme_is_dark"
    }
}