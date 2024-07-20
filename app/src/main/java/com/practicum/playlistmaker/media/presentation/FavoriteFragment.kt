package com.practicum.playlistmaker.media.presentation

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.main.presentation.TrackAdapter
import com.practicum.playlistmaker.player.presentation.PlayerFragment
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker2.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModel()
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteRecycler = view.findViewById<RecyclerView>(R.id.favorite_recycler)
        val emptyListView = view.findViewById<LinearLayout>(R.id.emptyView)
        val adapter = TrackAdapter()
        adapter.itemClickListener = { _, track ->
            if (clickDebounce()) openPlayer(track)
        }
        favoriteRecycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteTracksFlow.collect {
                    if (it.isEmpty()) {
                        favoriteRecycler.isVisible = false
                        emptyListView.isVisible = true
                    } else {
                        adapter.trackList = it
                        adapter.notifyDataSetChanged()
                        favoriteRecycler.isVisible = true
                        emptyListView.isVisible = false
                    }
                }
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickDebounceJob?.cancel()
            clickDebounceJob = lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun openPlayer(track: Track) {
        findNavController().navigate(R.id.playerFragment, bundleOf(PlayerFragment.TRACK_KEY to track))
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}