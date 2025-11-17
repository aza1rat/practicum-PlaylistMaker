package ru.aza1rat.playlistmaker.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.view_holder.TrackViewHolder

class TrackAdapter(private val onTrackClickListener: OnTrackClickListener? = null) : RecyclerView.Adapter<TrackViewHolder>(){
    fun interface OnTrackClickListener{
        fun onTrackClick(track: Track)
    }

    var trackList: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder.Companion.from(parent)

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            onTrackClickListener?.onTrackClick(trackList[position])
        }
    }

    override fun getItemCount(): Int = trackList.size
}