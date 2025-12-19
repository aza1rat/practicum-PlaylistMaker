package ru.aza1rat.playlistmaker.util.ui

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

fun getNavController(activity: FragmentActivity, @IdRes fragmentId: Int): NavController {
    val navHostFragment = activity.supportFragmentManager.findFragmentById(fragmentId) as NavHostFragment
    return navHostFragment.navController
}