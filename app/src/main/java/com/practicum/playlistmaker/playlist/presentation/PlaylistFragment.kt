package com.practicum.playlistmaker.playlist.presentation

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.main.presentation.TrackAdapter
import com.practicum.playlistmaker.media.presentation.PlaylistsFragment
import com.practicum.playlistmaker.player.presentation.PlayerFragment
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker2.R
import com.practicum.playlistmaker2.databinding.PlaylistBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private var _binding: PlaylistBinding? = null
    private val binding get() = _binding!!
    private var currentPlaylist: DetailedPlaylistModel? = null
    private val viewModel by viewModel<PlaylistViewModel>()
    private var behavior: BottomSheetBehavior<LinearLayout>? = null
    private val screenHeight by lazy { Resources.getSystem().displayMetrics.heightPixels }
    private val layoutListener = OnGlobalLayoutListener {
        behavior?.peekHeight = screenHeight - binding.topView.height - getStatusBarHeight() / 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistBinding.inflate(inflater, container, false)
        behavior = BottomSheetBehavior.from(binding.bottomView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = arguments?.getInt(PlaylistsFragment.PLAYLIST_ID_KEY)
        if (playlistId != null)  viewModel.getPlaylistById(playlistId)
        val trackAdapter = TrackAdapter()
        trackAdapter.itemClickListener = { _, track ->
            findNavController().navigate(
                R.id.playerFragment,
                bundleOf(PlayerFragment.TRACK_KEY to track)
            )
        }
        trackAdapter.onLongClickListener = { _, track ->
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.delete_track_dialogue)
                .setPositiveButton(R.string.yes_dialogue) { dialogue, _ ->
                    playlistId?.let { viewModel.deleteTrackFromPlaylist(track.trackId, it) }
                        ?: dialogue.dismiss()
                }
                .setNegativeButton(R.string.no_dialogue) { dialogue, _ -> dialogue.dismiss() }
                .show()
        }
        binding.playlistList.adapter = trackAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playlistFlow?.collect {
                    trackAdapter.trackList = it.trackList
                    trackAdapter.notifyDataSetChanged()
                    currentPlaylist = it
                    if (currentPlaylist?.description.isNullOrBlank()) binding.playlistDescription.visibility =
                        View.GONE
                    binding.playlistName.text = it.name
                    binding.playlistDescription.text = it.description
                    binding.playlistCover.setPadding(0)
                    Glide.with(this@PlaylistFragment)
                        .load(it.cover)
                        .centerCrop()
                        .placeholder(R.drawable.ic_vector)
                        .error(R.drawable.ic_vector)
                        .into(binding.playlistCover)
                    binding.tracksTimeMinutes.text = requireContext().resources.getQuantityString(
                        R.plurals.minutes_count,
                        it.totalTime,
                        it.totalTime
                    )
                    binding.playlistTracksCount.text = requireContext().resources.getQuantityString(
                        R.plurals.tracks_count,
                        it.count,
                        it.count
                    )
                }
            }
        }
        binding.returnButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playlistMore.setOnClickListener {
            if (currentPlaylist != null) {
                PlaylistDetailsFragment.newInstance(currentPlaylist!!)
                    .show(parentFragmentManager, null)
            }
        }
        binding.playlistShare.setOnClickListener {
            if (currentPlaylist?.trackList.isNullOrEmpty()) {
                showToast(R.string.playlist_is_empty)
            } else {
                val intent = viewModel.getShareIntent(
                    currentPlaylist?.trackList ?: emptyList(),
                    "text/plain",
                    requireContext()
                )
                startActivity(intent)
            }
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else Rect().apply { requireActivity().window.decorView.getWindowVisibleDisplayFrame(this) }.top
    }
}