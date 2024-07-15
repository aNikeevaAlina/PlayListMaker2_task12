package com.practicum.playlistmaker.media.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker2.databinding.PlaylistItemBinding

class PlaylistAdapter(private val onClick: (Int) -> Unit) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlists: List<PlaylistModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = playlists[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(list: List<PlaylistModel>) {
        playlists = list
        notifyDataSetChanged()
    }
}