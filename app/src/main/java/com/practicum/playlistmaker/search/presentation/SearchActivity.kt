package com.practicum.playlistmaker.search.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.practicum.playlistmaker.main.presentation.TrackAdapter
import com.practicum.playlistmaker.player.presentation.PlayerActivity
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker2.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var linearNothingFound: LinearLayout
    private lateinit var linearNoInternet: LinearLayout

    private lateinit var historySearchGroup: ConstraintLayout

    private lateinit var updateButton: Button
    private lateinit var clearButton: Button
    private lateinit var cleanHistoryButton: Button

    private lateinit var recycler: RecyclerView
    private lateinit var recyclerViewHistory: RecyclerView

    private lateinit var returnItemImageView: ImageView

    private lateinit var inputEditText: EditText
    private lateinit var loadingGroup: FrameLayout

    private var lastRequest: String = ""
    private var isClickAllowed = true

    private var adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.searchTracks(lastRequest) }
    private val viewModel: SearchViewModel by viewModel()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findItems()
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
        viewModel.historyState.observe(this) {
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
        viewModel.searchState.observe(this) {
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
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, lastRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.search_content)
        val text = savedInstanceState.getString(PRODUCT_AMOUNT)
        if (!text.isNullOrEmpty()) {
            inputEditText.setText(text)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    private fun addButtonListeners() {
        // Кнопка обновить запрос, когда нет сети
        updateButton.setOnClickListener {
            viewModel.searchTracks(lastRequest)
        }
        // Кнопка назад
        returnItemImageView.setOnClickListener {
            finish()
        }
        // Кнопка очистить поле ввода
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(currentFocus ?: View(this))
            linearNothingFound.isVisible = false
            linearNoInternet.isVisible = false
        }
    }

    private fun addChangeListeners() {
        // Реагирует на смену фокуса в EditText
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                historySearchGroup.isVisible = viewModel.historyState.value is HistoryUiState.NotEmpty
            }
        }
        // Реагирует на ввод текста в EditText
        inputEditText.requestFocus()

        inputEditText.doOnTextChanged { text, start, before, count ->
            lastRequest = text.toString()
            if (lastRequest.isNotEmpty()) searchDebounce()
            clearButton.isVisible = inputEditText.text.isNotEmpty()
            recycler.isVisible = inputEditText.text.isNotEmpty()
            linearNothingFound.isVisible = false
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) {
                historySearchGroup.isVisible = viewModel.historyState.value is HistoryUiState.NotEmpty
                linearNoInternet.isVisible = false
            } else {
                historySearchGroup.isVisible = false
                recycler.isVisible = true
            }
        }
    }

    private fun findItems() {
        linearNothingFound = findViewById(R.id.error_block_nothing_found)
        linearNoInternet = findViewById(R.id.error_block_setting)
        clearButton = findViewById(R.id.exit)
        updateButton = findViewById(R.id.button_update)
        returnItemImageView = findViewById(R.id.return_n)
        cleanHistoryButton = findViewById(R.id.clean_history_button)
        inputEditText = findViewById(R.id.search_content)
        historySearchGroup = findViewById(R.id.history_search_group)
        recycler = findViewById(R.id.recyclerView)
        recyclerViewHistory = findViewById(R.id.recycler_view_history)
        loadingGroup = findViewById(R.id.loadingGroup)
    }

    private fun recyclerSetting() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = historyAdapter
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java).putExtra("track", track)
        startActivity(intent)
    }
}
