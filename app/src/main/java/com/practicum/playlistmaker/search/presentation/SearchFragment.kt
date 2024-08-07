package com.practicum.playlistmaker.search.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.practicum.playlistmaker.main.presentation.TrackAdapter
import com.practicum.playlistmaker.player.presentation.PlayerFragment
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker2.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var linearNothingFound: LinearLayout
    private lateinit var linearNoInternet: LinearLayout

    private lateinit var historySearchGroup: ConstraintLayout

    private lateinit var updateButton: Button
    private lateinit var clearButton: Button
    private lateinit var cleanHistoryButton: Button

    private lateinit var recycler: RecyclerView
    private lateinit var recyclerViewHistory: RecyclerView

    private lateinit var inputEditText: EditText
    private lateinit var loadingGroup: FrameLayout

    private var lastRequest: String = ""
    private var isClickAllowed = true

    private var adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()

    private val viewModel: SearchViewModel by viewModel()

    private var clickDebounceJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findItems(view)
        recyclerSetting()
        addChangeListeners()
        addButtonListeners()

        // Кнопка очищения истории поиска
        cleanHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        historyAdapter.itemClickListener = { _, track ->
            if (clickDebounce()) openPlayer(track)
        }
        adapter.itemClickListener = { _, track ->
            viewModel.addTrackToHistory(track)
            if (clickDebounce()) openPlayer(track)
        }

        observeHistory()
        observeSearchState()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeHistory() {
        viewModel.historyState.observe(viewLifecycleOwner) {
            historySearchGroup.isVisible = it is HistoryUiState.NotEmpty && inputEditText.text.isNullOrEmpty()
            when (it) {
                HistoryUiState.Empty -> {
                    historyAdapter.trackList = emptyList()
                }
                is HistoryUiState.NotEmpty -> {
                    historyAdapter.trackList = it.tracks
                }
            }
            historyAdapter.notifyDataSetChanged()

        }
    }

    private fun observeSearchState() {
        viewModel.searchState.observe(viewLifecycleOwner) {
            when (it) {
                is SearchUiState.Error -> {
                    linearNoInternet.isVisible = true
                    linearNothingFound.isVisible = false
                    recycler.isVisible = false
                    historySearchGroup.isVisible = false
                    loadingGroup.isVisible = false
                }
                SearchUiState.Loading -> {
                    loadingGroup.isVisible = true
                    recycler.isVisible = false
                    linearNothingFound.isVisible = false
                    linearNoInternet.isVisible = false
                    historySearchGroup.isVisible = false
                }
                is SearchUiState.Success -> {
                    if (it.tracks.isNotEmpty()) {
                        adapter.trackList = it.tracks
                        adapter.notifyDataSetChanged()
                    }
                    linearNothingFound.isVisible = it.tracks.isEmpty()
                    recycler.isVisible = it.tracks.isNotEmpty()
                    linearNoInternet.isVisible = false
                    historySearchGroup.isVisible = false
                    loadingGroup.isVisible = false
                }
            }
        }
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, lastRequest)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val inputEditText = view?.findViewById<EditText>(R.id.search_content)
        val text = savedInstanceState?.getString(PRODUCT_AMOUNT)
        if (!text.isNullOrEmpty()) {
            inputEditText?.setText(text)
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

    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    private fun addButtonListeners() {
        // Кнопка обновить запрос, когда нет сети
        updateButton.setOnClickListener {
            viewModel.searchTracks(lastRequest)
        }
        // Кнопка очистить поле ввода
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(requireActivity().currentFocus ?: View(requireContext()))
            linearNothingFound.isVisible = false
            linearNoInternet.isVisible = false
        }
    }

    private fun addChangeListeners() {
        // Реагирует на смену фокуса в EditText
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                historySearchGroup.isVisible = viewModel.historyState.value is HistoryUiState.NotEmpty
                recycler.isVisible = false

            }
        }
        // Реагирует на ввод текста в EditText
        inputEditText.requestFocus()

        inputEditText.doOnTextChanged { text, start, before, count ->
            lastRequest = text.toString()
            viewModel.searchDebounce(lastRequest)
            clearButton.isVisible = inputEditText.text.isNotEmpty()
            recycler.isVisible = inputEditText.text.isNotEmpty()
            linearNothingFound.isVisible = false
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) {
                historySearchGroup.isVisible = viewModel.historyState.value is HistoryUiState.NotEmpty
                recycler.isVisible = false
                linearNoInternet.isVisible = false
            } else {
                historySearchGroup.isVisible = false
                recycler.isVisible = true
            }
        }
    }

    private fun findItems(view: View) {
        linearNothingFound = view.findViewById(R.id.error_block_nothing_found)
        linearNoInternet = view.findViewById(R.id.error_block_setting)
        clearButton = view.findViewById(R.id.exit)
        updateButton = view.findViewById(R.id.button_update)
        cleanHistoryButton = view.findViewById(R.id.clean_history_button)
        inputEditText = view.findViewById(R.id.search_content)
        historySearchGroup = view.findViewById(R.id.history_search_group)
        recycler = view.findViewById(R.id.recyclerView)
        recyclerViewHistory = view.findViewById(R.id.recycler_view_history)
        loadingGroup = view.findViewById(R.id.loadingGroup)
    }

    private fun recyclerSetting() {
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewHistory.adapter = historyAdapter
    }

    private fun openPlayer(track: Track) {
        findNavController().navigate(R.id.playerFragment, bundleOf(PlayerFragment.TRACK_KEY to track))
    }
}
