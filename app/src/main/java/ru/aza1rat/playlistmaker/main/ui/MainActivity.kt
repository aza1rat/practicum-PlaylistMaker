package ru.aza1rat.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.aza1rat.playlistmaker.databinding.ActivityMainBinding
import ru.aza1rat.playlistmaker.media_library.ui.MediaLibraryActivity
import ru.aza1rat.playlistmaker.search.ui.SearchActivity
import ru.aza1rat.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.search.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
        binding.mediaLibrary.setOnClickListener {
            startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
        }
        binding.settings.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}