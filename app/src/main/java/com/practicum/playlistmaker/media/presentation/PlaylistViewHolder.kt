package com.practicum.playlistmaker.media.presentation

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker2.R
import com.practicum.playlistmaker2.databinding.PlaylistItemBinding

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val onClick: (Int)-> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PlaylistModel) {
        binding.root.setOnClickListener {
            onClick.invoke(item.id)
        }
        binding.playlistName.text = item.name
        binding.playlistTracksCount.text = itemView.resources.getQuantityString(R.plurals.tracks_count, item.count, item.count)
        Glide.with(binding.playlistCover)
            .load(item.cover)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.margin_recycler_element))
            )
            .placeholder(R.drawable.ic_vector)
            .error(R.drawable.ic_vector)
            .into(binding.playlistCover)
    }
}