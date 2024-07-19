package com.practicum.playlistmaker.addToPlaylist.presentation

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker2.R
import com.practicum.playlistmaker2.databinding.FragmentAddToPlaylistBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddToPlaylistFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModel<AddToPlaylistViewModel>()
    private var _binding: FragmentAddToPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddToPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track =
            BundleCompat.getParcelable(arguments ?: bundleOf(), ARGS_KEY, Track::class.java)!!
        val adapter = AddToPlaylistAdapter {
            viewModel.addTrackToPlaylist(track, it)
        }
        viewModel.trackAddingResultFlow.observe(this) {
            when (it) {
                is PlaylistState.Success -> {
                    Toast.makeText(
                        requireContext(), requireContext().getString(
                            R.string.adding_to_playlist_success, it.playlistName
                        ), Toast.LENGTH_LONG
                    ).show()
                    dismiss()
                }
                is PlaylistState.Unavailable -> {
                    Toast.makeText(
                        requireContext(), requireContext().getString(
                            R.string.adding_to_playlist_declined, it.playlistName
                        ), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.playlistList.adapter = adapter
        viewModel.playlistsFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                adapter.updatePlaylists(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.newPlaylistButton.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.createPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGS_KEY = "args"

        @JvmStatic
        fun newInstance(args: Parcelable): AddToPlaylistFragment {
            return AddToPlaylistFragment().apply {
                arguments = bundleOf(ARGS_KEY to args)
            }
        }
    }
}