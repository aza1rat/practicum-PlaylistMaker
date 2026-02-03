package ru.aza1rat.playlistmaker.util.ui

import android.view.View
import androidx.core.view.isVisible

fun <T : View?> showMainView(vararg mainViews: View): (T?)-> Unit {
    return {
        view: T? ->
            for (mainView in mainViews) {
                mainView.apply { this.isVisible = this == view}
            }
    }
}