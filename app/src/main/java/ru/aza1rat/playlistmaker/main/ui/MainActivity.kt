package ru.aza1rat.playlistmaker.main.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.ui.setupWithNavController
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.ActivityMainBinding
import ru.aza1rat.playlistmaker.util.ui.getNavController

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navController = this.getNavController(R.id.fragmentContainer)
        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment -> {
                    setVisibilityToBottomNavigation(false)
                }
                else -> {
                    setVisibilityToBottomNavigation(true)
                }
            }
        }
    }

    private fun setVisibilityToBottomNavigation(isVisible: Boolean) {
        binding.bottomNavigation.isVisible = isVisible
        binding.bottomNavigationDivider.isVisible = isVisible
    }
}