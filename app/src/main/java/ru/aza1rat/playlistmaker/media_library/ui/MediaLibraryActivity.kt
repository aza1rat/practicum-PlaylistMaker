package ru.aza1rat.playlistmaker.media_library.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.ActivityMediaLibraryBinding
import ru.aza1rat.playlistmaker.media_library.ui.adapter.MediaLibraryViewPagerAdapter

class MediaLibraryActivity : AppCompatActivity() {
    private var _binding: ActivityMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.libraryPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager,lifecycle)
        tabMediator = TabLayoutMediator(binding.libraryTabs, binding.libraryPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_library_tab_favourites)
                1 -> tab.text = getString(R.string.media_library_tab_playlists)
            }
        }
        tabMediator.attach()
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        tabMediator.detach()
        super.onDestroy()
    }
}