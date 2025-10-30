package ru.aza1rat.playlistmaker

import android.app.Application
import ru.aza1rat.playlistmaker.data.BaseThemeControl

class App : Application() {
    lateinit var themeControl: BaseThemeControl
        private set

    override fun onCreate() {
        super.onCreate()
        Creator.init(applicationContext)
        themeControl = Creator.createThemeControl()
        themeControl.setDarkTheme(themeControl.savedThemeIsDark())
    }
}