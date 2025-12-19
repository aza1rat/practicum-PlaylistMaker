package ru.aza1rat.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentMediaLibraryBinding
import ru.aza1rat.playlistmaker.media_library.ui.adapter.MediaLibraryViewPagerAdapter

class MediaLibraryFragment: Fragment() {
    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.libraryPager.adapter = MediaLibraryViewPagerAdapter(childFragmentManager,lifecycle)
        tabMediator = TabLayoutMediator(binding.libraryTabs, binding.libraryPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_library_tab_favourites)
                1 -> tab.text = getString(R.string.media_library_tab_playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        _binding = null
        tabMediator.detach()
        super.onDestroyView()
    }
}