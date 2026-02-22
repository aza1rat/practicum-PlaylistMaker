package ru.aza1rat.playlistmaker.media_library.ui.view_holder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount
import ru.aza1rat.playlistmaker.util.ui.PluralUtil

abstract class BasePlaylistTracksCountViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(playlist: PlaylistTracksCount)

    fun getPluralTracksCount(tracksCount: Int): String {
        return PluralUtil.format(PluralUtil.TRACK_PATTERN,tracksCount)
    }
}