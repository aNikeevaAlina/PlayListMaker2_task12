package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.practicum.playlistmaker2.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    var inputSaveText: String = ""
    private var isClickAllowed = true

    private val itunesService = ApiForItunes.retrofit.create(ApiForItunes::class.java)

    private var trackList = ArrayList<Track>()
    private var adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()

    private val sharedPrefs by lazy { getSharedPreferences(HISTORY_SEARCH, MODE_PRIVATE) }
    private val searchHistory by lazy { SearchHistory(sharedPrefs) }
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findItems()
        recyclerSetting()
        addChangeListeners()
        addButtonListeners()

        historySearchGroup.isVisible = searchHistory.get().isNotEmpty()

        // Кнопка очищения истории поиска
        cleanHistoryButton.setOnClickListener {
            sharedPrefs.edit().clear().apply()
            historyAdapter.trackList = searchHistory.get().toCollection(ArrayList())
            historyAdapter.notifyDataSetChanged()
            historySearchGroup.visibility = View.GONE
        }

        adapter.trackList = trackList
        historyAdapter.trackList = searchHistory.get().toCollection(ArrayList())
        historyAdapter.notifyDataSetChanged()

        inputSaveText = inputEditText.text.toString()
        historyAdapter.itemClickListener = { position, track ->
            if (clickDebounce()) openPlayer(track)
        }
        adapter.itemClickListener = { position, track ->
            searchHistory.add(track)
            if (clickDebounce()) openPlayer(track)
        }
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val HISTORY_SEARCH = "HISTORY_SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, inputSaveText)
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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchTracks() {
        loadingGroup.isVisible = true
        recycler.isVisible = false
        linearNothingFound.isVisible = false
        linearNoInternet.isVisible = false
        recyclerViewHistory.isVisible = false
        itunesService.search(lastRequest)
            .enqueue(object : Callback<ItunesResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.clear()
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                recycler.visibility = View.VISIBLE
                                linearNothingFound.visibility = View.GONE
                            } else {
                                linearNothingFound.visibility = View.VISIBLE
                                recycler.visibility = View.GONE
                            }
                            linearNoInternet.visibility = View.GONE
                        }

                        else -> {
                            linearNoInternet.visibility = View.VISIBLE
                            linearNothingFound.visibility = View.GONE
                            recycler.visibility = View.GONE
                        }
                    }
                    loadingGroup.isVisible = false
                    historySearchGroup.visibility = View.GONE
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    linearNoInternet.visibility = View.VISIBLE
                    linearNothingFound.visibility = View.GONE
                    recycler.visibility = View.GONE
                    historySearchGroup.visibility = View.GONE
                    loadingGroup.isVisible = false
                }
            })
    }

    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    private fun addButtonListeners() {
        // Кнопка обновить запрос, когда нет сети
        updateButton.setOnClickListener {
            searchTracks()
        }
        // Кнопка назад
        returnItemImageView.setOnClickListener {
            finish()
        }
        // Кнопка очистить поле ввода
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(currentFocus ?: View(this))
            trackList.clear()
            adapter.notifyDataSetChanged()
            historyAdapter.trackList = searchHistory.get().toCollection(ArrayList())
            historyAdapter.notifyDataSetChanged()
            linearNothingFound.visibility = View.GONE
            linearNoInternet.visibility = View.GONE
        }
    }

    private fun addChangeListeners() {
        // Реагирует на смену фокуса в EditText
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) {
                historySearchGroup.isVisible = searchHistory.get().isNotEmpty()
            }
        }
        // Реагирует на ввод текста в EditText
        inputEditText.requestFocus()
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                lastRequest = s.toString()
                searchDebounce()
                clearButton.isVisible = inputEditText.text.isNotEmpty()
                recycler.isVisible = inputEditText.text.isNotEmpty()
                linearNothingFound.isVisible = false
                if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) {
                    historySearchGroup.isVisible = searchHistory.get().isNotEmpty()
                    linearNoInternet.isVisible = false
                    linearNothingFound.isVisible = false
                } else {
                    historySearchGroup.isVisible = false
                    recycler.isVisible = true
                }
                inputSaveText = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun findItems() {
        linearNothingFound = findViewById(R.id.error_block_nothing_found)
        linearNoInternet = findViewById(R.id.error_block_setting)
        clearButton = findViewById<Button>(R.id.exit)
        updateButton = findViewById(R.id.button_update)
        returnItemImageView = findViewById<ImageView>(R.id.return_n)
        cleanHistoryButton = findViewById(R.id.clean_history_button)
        inputEditText = findViewById(R.id.search_content)
        historySearchGroup = findViewById(R.id.history_search_group)
        recycler = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewHistory = findViewById<RecyclerView>(R.id.recycler_view_history)
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
