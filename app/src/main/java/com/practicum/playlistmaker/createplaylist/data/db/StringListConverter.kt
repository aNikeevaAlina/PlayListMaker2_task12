package com.practicum.playlistmaker.createplaylist.data.db

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    @TypeConverter
    fun fromList(list: List<String>): String {
        Log.d("QQQQ list", list.toString())
        Log.d("QQQQ list", list.joinToString(SEPARATOR).toString())
        return list.filter { it.isNotBlank() }.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun fromString(string: String): List<String> {
        Log.d("QQQQ string", string)
        Log.d("QQQQ string", string.split(SEPARATOR).toString())
        return if (string.isEmpty()) emptyList()
        else string.split(SEPARATOR)
    }

    companion object {
        private const val SEPARATOR = "|"
    }
}