package ru.aza1rat.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.data.Track
import ru.aza1rat.playlistmaker.viewHolder.TrackViewHolder

class TrackAdapter(private val trackList: MutableList<Track>) : RecyclerView.Adapter<TrackViewHolder>(){
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
    }

    override fun getItemCount(): Int = trackList.size
}