package ru.aza1rat.playlistmaker.media_library.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.aza1rat.playlistmaker.media_library.ui.FavouriteTracksFragment
import ru.aza1rat.playlistmaker.media_library.ui.PlaylistsFragment

class MediaLibraryViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouriteTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

    override fun getItemCount() = 2

}