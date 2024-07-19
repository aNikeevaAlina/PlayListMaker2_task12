package com.practicum.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker2.R
import com.practicum.playlistmaker2.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker2.databinding.FragmentPlaylistsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlaylistAdapter {

        }

        binding.playlistsRecycler.adapter = adapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.createPlaylistFragment)
        }

        viewModel.playlistsFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.noPlaylistsView.isVisible = it.isEmpty()
                binding.playlistsRecycler.isVisible = it.isNotEmpty()
                adapter.updatePlaylists(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}