package com.example.tv_video_list.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>): String {
        return genreIds.joinToString(separator = ",")
    }

    @TypeConverter
    fun toGenreIds(data: String): List<Int> {
        return if (data.isEmpty()) listOf() else data.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun fromListString(listString: List<String>): String {
        return listString.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListString(data: String): List<String> {
        return if (data.isEmpty()) listOf() else data.split(",")
    }
}