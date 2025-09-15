package ru.aza1rat.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {

    var darkTheme = false
        private set

    private fun currentThemeIsDark(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }

            else -> false
        }
    }

    override fun onCreate() {
        super.onCreate()
        val themeIsDark = currentThemeIsDark()
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(CURRENT_THEME_KEY, themeIsDark)
        if (darkTheme != themeIsDark) switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit {
            putBoolean(
                CURRENT_THEME_KEY, darkTheme
            )
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "playlistmaker_preferences"
        const val CURRENT_THEME_KEY = "current_theme_is_dark"
    }
}