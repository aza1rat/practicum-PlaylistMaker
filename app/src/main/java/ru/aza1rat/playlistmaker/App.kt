package ru.aza1rat.playlistmaker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ru.aza1rat.playlistmaker.di.dataModule
import ru.aza1rat.playlistmaker.di.interactorModule
import ru.aza1rat.playlistmaker.di.repositoryModule
import ru.aza1rat.playlistmaker.di.viewModelModule
import ru.aza1rat.playlistmaker.settings.data.BaseThemeControl

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        val themeControl: BaseThemeControl = getKoin().get()
        themeControl.setDarkTheme(themeControl.savedThemeIsDark())
    }
}