package com.practicum.playlistmaker.search.di

import android.app.Application
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.search.data.ApiForItunes
import com.practicum.playlistmaker.search.data.NetworkServiceImpl
import com.practicum.playlistmaker.search.data.SearchHistoryImpl
import com.practicum.playlistmaker.search.domain.NetworkService
import com.practicum.playlistmaker.search.domain.SearchHistory
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {
    viewModelOf(::SearchViewModel)

    factory { SearchInteractor(get(), get()) }

    factory<NetworkService> { NetworkServiceImpl(get()) }

    factory<ApiForItunes> { get<Retrofit>().create(ApiForItunes::class.java) }

    factory<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ApiForItunes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<SearchHistory> {
        SearchHistoryImpl(
            androidApplication().getSharedPreferences(
                App.SAVE_THEME,
                Application.MODE_PRIVATE
            )
        )
    }
}