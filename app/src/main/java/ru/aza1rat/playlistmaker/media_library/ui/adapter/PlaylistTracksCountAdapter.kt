package ru.aza1rat.playlistmaker.media_library.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount
import ru.aza1rat.playlistmaker.media_library.ui.view_holder.BasePlaylistTracksCountViewHolder
import ru.aza1rat.playlistmaker.media_library.ui.view_holder.GridPlaylistTracksCountViewHolder
import ru.aza1rat.playlistmaker.media_library.ui.view_holder.SheetPlaylistTracksCountViewHolder

class PlaylistTracksCountAdapter(
    private val playlistViewType: ViewType,
    private val onPlaylistClickListener: OnPlaylistClickListener? = null
) : RecyclerView.Adapter<BasePlaylistTracksCountViewHolder>() {
    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlist: PlaylistTracksCount)
    }

    var playlists: List<PlaylistTracksCount> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasePlaylistTracksCountViewHolder {
        return when (playlistViewType) {
            ViewType.PLAYLIST_GRID_ITEM -> GridPlaylistTracksCountViewHolder.from(parent)
            ViewType.PLAYLIST_SHEET_ITEM -> SheetPlaylistTracksCountViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(
        holder: BasePlaylistTracksCountViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    enum class ViewType {
        PLAYLIST_GRID_ITEM,
        PLAYLIST_SHEET_ITEM
    }
}