package ru.aza1rat.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<MaterialButton>(R.id.search)
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
        val mediaLibraryButton = findViewById<MaterialButton>(R.id.media_library)
        mediaLibraryButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
        }
        val settingsButton = findViewById<MaterialButton>(R.id.settings)
        settingsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}