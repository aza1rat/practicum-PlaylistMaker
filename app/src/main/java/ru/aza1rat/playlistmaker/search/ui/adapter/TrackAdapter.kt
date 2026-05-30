package ru.aza1rat.playlistmaker.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.view_holder.TrackViewHolder

class TrackAdapter(
    private val onTrackClickListener: OnTrackClickListener? = null,
    private val onTrackLongClickListener: OnTrackLongClickListener? = null
) : RecyclerView.Adapter<TrackViewHolder>() {
    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface OnTrackLongClickListener {
        fun onTrackLongClick(track: Track)
    }

    var trackList: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onTrackClickListener?.onTrackClick(trackList[position])
        }
        holder.itemView.setOnLongClickListener {
            onTrackLongClickListener?.onTrackLongClick(trackList[position])
            true
        }
    }

    override fun getItemCount(): Int = trackList.size
}