package ru.aza1rat.playlistmaker.util.ui

import android.os.Build
import android.util.DisplayMetrics
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.getScreenWidth(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return this.windowManager.currentWindowMetrics.bounds.width()
    } else {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}