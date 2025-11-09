package ru.aza1rat.playlistmaker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.domain.model.Track
import ru.aza1rat.playlistmaker.presentation.viewholder.TrackViewHolder

class TrackAdapter(private val onTrackClickListener: OnTrackClickListener? = null) : RecyclerView.Adapter<TrackViewHolder>(){
    fun interface OnTrackClickListener{
        fun onTrackClick(track: Track)
    }

    var trackList: List<Track> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track,parent,false)
        return TrackViewHolder(view)
    }

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