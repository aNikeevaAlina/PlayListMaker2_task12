package com.practicum.playlistmaker.editPlaylist.presentation

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.createplaylist.presentation.CreatePlaylistFragment
import com.practicum.playlistmaker.playlist.presentation.PlaylistDetailsFragment
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker2.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()
    lateinit var playlist : DetailedPlaylistModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylistButton.text = getString(R.string.save_text)
        binding.toolbarText.text = getString(R.string.edit_playlist_toolbar_text)
        playlist = BundleCompat.getParcelable(
            arguments ?: bundleOf(),
            PlaylistDetailsFragment.ARGS_KEY,
            DetailedPlaylistModel::class.java
        )!!
        imageUri = playlist.cover?.toUri()
        binding.editText.setText(playlist.name)
        binding.descriptionEditText.setText(playlist.description)
        if (!playlist.cover.isNullOrBlank()) binding.playlistCover.setPadding(0)
        Glide.with(this)
            .load(playlist.cover)
            .placeholder(R.drawable.add_photo)
            .error(R.drawable.add_photo)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_recycler_element))
            )
            .into(binding.playlistCover)
    }

    override fun onMainButtonPressed() {
        val playlistName = binding.editText.text.toString()
        val description = binding.descriptionEditText.text?.toString()
        viewModel.saveUpdates(
            playlist,
            playlistName,
            if (description.isNullOrBlank()) null else description,
            imageUri
        )
        findNavController().popBackStack()
    }

    override fun onBackPressed() {
        findNavController().popBackStack()
    }
}