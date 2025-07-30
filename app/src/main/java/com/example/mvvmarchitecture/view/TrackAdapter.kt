package com.example.mvvmarchitecture.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmarchitecture.model.Track
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.RecycleViewTrackBinding

class TrackAdapter(
    val listOfTrack: MutableList<Track>,
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    fun addTracks(newTracks: List<Track>) {
        val startPos = listOfTrack.size
        listOfTrack.addAll(newTracks)
        notifyItemRangeInserted(startPos, newTracks.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        val binding = RecycleViewTrackBinding.bind(holder.itemView)
        binding.tvTrackTitle.text = listOfTrack.get(position).songName
        binding.tvReleaseYear.text = listOfTrack.get(position).releaseDate
        val imageView : ImageView = binding.imageTrack

        Glide.with(holder.itemView).load(listOfTrack[position].imageUrl).into(imageView)
    }

    override fun getItemCount() = listOfTrack.size


    inner class TrackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}