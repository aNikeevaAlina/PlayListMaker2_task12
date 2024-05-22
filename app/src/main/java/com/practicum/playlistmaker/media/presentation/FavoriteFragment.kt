package com.practicum.playlistmaker.media.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker2.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}