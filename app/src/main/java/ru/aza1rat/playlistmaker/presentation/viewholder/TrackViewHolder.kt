package ru.aza1rat.playlistmaker.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.domain.model.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artworkImageView = itemView.findViewById<ShapeableImageView>(R.id.trackArtwork)
    private val nameTextView = itemView.findViewById<TextView>(R.id.trackName)
    private val artistTextView = itemView.findViewById<TextView>(R.id.trackArtist)
    private val timeTextView = itemView.findViewById<TextView>(R.id.trackTime)

    fun bind(track: Track) {
        nameTextView.text = track.trackName
        artistTextView.text = track.artistName
        timeTextView.text = track.trackTime
        Glide.with(itemView).load(track.artworkUrl100)
            .placeholder(R.drawable.ph_track_45)
            .into(artworkImageView)
    }
}