package ru.aza1rat.playlistmaker.util.ui

import android.icu.text.PluralFormat
import android.icu.util.ULocale

object PluralUtil {
    const val TRACK_PATTERN = "zero{треков} one{трек} few{трека} many{треков} other{треков}"
    private val locale = ULocale("ru")

    fun format(pattern: String, count: Int): String {
        return count.toString() + " " + PluralFormat(locale,pattern).format(count)
    }
}