package com.practicum.playlistmaker.playlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker2.R
import com.practicum.playlistmaker2.databinding.FragmentPlaylistDetailsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.removingState.collect {
                    if (it) {
                        dismiss()
                        findNavController().popBackStack(R.id.mediaFragment, false)
                    }
                }
            }
        }

        val playlist = BundleCompat.getParcelable(
            arguments ?: bundleOf(),
            ARGS_KEY,
            DetailedPlaylistModel::class.java
        )!!

        binding.playlistItemSmall.playlistName.text = playlist.name
        binding.playlistItemSmall.playlistTracksCount.text =
            requireContext().resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.count,
                playlist.count
            )
        Glide.with(binding.playlistItemSmall.playlistCover)
            .load(playlist.cover)
            .transform(
                CenterCrop(),
                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.margin_recycler_element))
            )
            .placeholder(R.drawable.ic_vector)
            .error(R.drawable.ic_vector)
            .into(binding.playlistItemSmall.playlistCover)

        binding.shareButton.setOnClickListener {
            if (playlist.trackList.isEmpty()) {
                Toast.makeText(requireContext(), R.string.playlist_is_empty, Toast.LENGTH_LONG).show()
            } else {
                val intent = viewModel.getShareIntent(
                    playlist.trackList,
                    "text/plaint",
                    requireContext()
                )
                startActivity(intent)
            }
            dismiss()
        }
        binding.editPlaylistButton.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.editPlaylistFragment, bundleOf(ARGS_KEY to playlist))
        }
        binding.deletePlaylistButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(
                    getString(
                        R.string.want_to_delete_playlist,
                        playlist.name
                    )
                ) // Описание диалога
                .setNegativeButton(getString(R.string.no_dialogue)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes_dialogue)) { _, _ ->
                    viewModel.deletePlaylist(playlist)
                }
                .show()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARGS_KEY = "playlist"

        @JvmStatic
        fun newInstance(args: DetailedPlaylistModel): PlaylistDetailsFragment {
            return PlaylistDetailsFragment().apply {
                arguments = bundleOf(ARGS_KEY to args)
            }
        }
    }

}