package ru.aza1rat.playlistmaker.media_library.ui.view_holder

import android.icu.text.PluralFormat
import android.icu.text.PluralRules
import android.icu.util.ULocale
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.aza1rat.playlistmaker.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.aza1rat.playlistmaker.databinding.ItemPlaylistBinding
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount
import java.util.Locale

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