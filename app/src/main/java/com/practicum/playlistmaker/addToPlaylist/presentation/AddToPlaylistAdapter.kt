package com.practicum.playlistmaker.addToPlaylist.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker2.databinding.PlaylistItemSmallBinding

class AddToPlaylistAdapter(private val onClick: (Int)-> Unit): RecyclerView.Adapter<AddToPlaylistViewHolder>() {
    private var playlists: List<PlaylistModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddToPlaylistViewHolder {
        val binding = PlaylistItemSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddToPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddToPlaylistViewHolder, position: Int) {
        val item = playlists[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(playlistsList: List<PlaylistModel>) {
        playlists = playlistsList
        notifyDataSetChanged()
    }

}