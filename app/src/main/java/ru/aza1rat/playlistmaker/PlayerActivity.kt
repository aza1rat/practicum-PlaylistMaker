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
        val trackNameTextView = findViewById<MaterialTextView>(R.id.trackName)
        trackNameTextView.text = track.trackName
         val trackArtistTextView = findViewById<MaterialTextView>(R.id.trackArtist)
         trackArtistTextView.text = track.artistName
        val durationTextView = findViewById<MaterialTextView>(R.id.duration)
        durationTextView.text = track.formatTrackTime()
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val albumTextView = findViewById<MaterialTextView>(R.id.album)
        if (!hideGroupWhenNullValue(albumGroup,track.collectionName))
            albumTextView.text = track.collectionName
        val yearGroup = findViewById<Group>(R.id.yearGroup)
        val yearTextView = findViewById<MaterialTextView>(R.id.year)
        val releaseYear = track.getReleaseYear()
        if (!hideGroupWhenNullValue(yearGroup,releaseYear))
            yearTextView.text = releaseYear
        val genreTextView = findViewById<MaterialTextView>(R.id.genre)
        genreTextView.text = track.primaryGenreName
        val countryTextView = findViewById<MaterialTextView>(R.id.country)
        countryTextView.text = track.country
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