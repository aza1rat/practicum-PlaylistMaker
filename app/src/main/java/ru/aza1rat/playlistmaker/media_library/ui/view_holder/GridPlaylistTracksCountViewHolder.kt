package ru.aza1rat.playlistmaker.media_library.ui.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.aza1rat.playlistmaker.R
import com.bumptech.glide.Glide
import ru.aza1rat.playlistmaker.databinding.ItemPlaylistBinding
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount

class GridPlaylistTracksCountViewHolder(private val binding: ItemPlaylistBinding) :
    BasePlaylistTracksCountViewHolder(binding) {

    override fun bind(playlist: PlaylistTracksCount) {
        binding.playlistName.text = playlist.name
        binding.playlistTracksCount.text = getPluralTracksCount(playlist.count)
        Glide.with(itemView).load(playlist.coverUri)
            .placeholder(R.drawable.ph_track_45)
            .into(binding.playlistCover)
    }

    companion object {
        fun from(parent: ViewGroup): BasePlaylistTracksCountViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaylistBinding.inflate(inflater, parent, false)
            return GridPlaylistTracksCountViewHolder(binding)
        }
    }
}