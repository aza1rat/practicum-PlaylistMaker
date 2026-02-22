package ru.aza1rat.playlistmaker.media_library.ui.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.ItemSheetPlaylistBinding
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount

class SheetPlaylistTracksCountViewHolder(private val binding: ItemSheetPlaylistBinding) :
    BasePlaylistTracksCountViewHolder(binding) {
    override fun bind(playlist: PlaylistTracksCount, tracksCountText: String) {
        binding.playlistName.text = playlist.name
        binding.playlistTracksCount.text = tracksCountText
        Glide.with(itemView).load(playlist.coverUri)
            .placeholder(R.drawable.ph_track_45)
            .into(binding.playlistCover)
    }

    companion object {
        fun from(parent: ViewGroup): BasePlaylistTracksCountViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSheetPlaylistBinding.inflate(inflater, parent, false)
            return SheetPlaylistTracksCountViewHolder(binding)
        }
    }
}