package ru.aza1rat.playlistmaker.search.ui.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.ItemTrackBinding
import ru.aza1rat.playlistmaker.search.domain.model.Track

class TrackViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTime.text = track.trackTime
        Glide.with(itemView).load(track.artworkUrl100)
            .placeholder(R.drawable.ph_track_45)
            .into(binding.trackArtwork)
    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTrackBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}