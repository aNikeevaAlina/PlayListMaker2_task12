package com.practicum.playlistmaker.createplaylist.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<String>>() {}

    @TypeConverter
    fun fromList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(string: String): List<String> {
        return gson.fromJson(string, typeToken)
    }
}