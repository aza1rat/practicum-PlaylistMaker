package ru.aza1rat.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import ru.aza1rat.playlistmaker.data.Track

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val track = intent.getParcelableExtra<Track>(INTENT_TRACK_EXTRA_KEY)
        if (track == null) {
            finish()
            return
        }
        findViewById<MaterialTextView>(R.id.trackName).apply {
            this.text = track.trackName
        }
        findViewById<MaterialTextView>(R.id.trackArtist).apply {
            this.text = track.artistName
        }
        findViewById<MaterialTextView>(R.id.duration).apply {
            this.text = track.formatTrackTime()
        }
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        findViewById<MaterialTextView>(R.id.album).apply {
            if (!hideGroupWhenNullValue(albumGroup,track.collectionName))
                this.text = track.collectionName
        }
        val yearGroup = findViewById<Group>(R.id.yearGroup)
        findViewById<MaterialTextView>(R.id.year).apply {
            val releaseYear = track.getReleaseYear()
            if (!hideGroupWhenNullValue(yearGroup,releaseYear))
                this.text = releaseYear
        }
        findViewById<MaterialTextView>(R.id.genre).apply {
            this.text = track.primaryGenreName
        }
        findViewById<MaterialTextView>(R.id.country).apply {
            this.text = track.country
        }
        val coverImageView = findViewById<ImageView>(R.id.cover)
        Glide.with(this).load(track.getArtworkUrl512())
            .placeholder(R.drawable.ph_track_45)
            .into(coverImageView)

        val backImageButton = findViewById<ImageButton>(R.id.back)
        backImageButton.setOnClickListener { finish() }
    }

    private fun hideGroupWhenNullValue(group: Group,value: String?): Boolean {
        group.isVisible = value != null
        return !group.isVisible
    }

    companion object {
        const val INTENT_TRACK_EXTRA_KEY = "TRACK"
    }
}