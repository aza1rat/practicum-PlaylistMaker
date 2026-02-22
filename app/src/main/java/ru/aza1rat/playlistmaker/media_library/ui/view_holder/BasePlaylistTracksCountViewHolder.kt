package ru.aza1rat.playlistmaker.media_library.ui.view_holder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount

abstract class BasePlaylistTracksCountViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(playlist: PlaylistTracksCount, tracksCountText: String)
}