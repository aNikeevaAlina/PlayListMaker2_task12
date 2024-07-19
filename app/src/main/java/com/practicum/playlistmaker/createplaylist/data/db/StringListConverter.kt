package com.practicum.playlistmaker.createplaylist.data.db

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.filter { it.isNotBlank() }.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun fromString(string: String): List<String> {
        return if (string.isEmpty()) emptyList()
        else string.split(SEPARATOR)
    }

    companion object {
        private const val SEPARATOR = "|"
    }
}