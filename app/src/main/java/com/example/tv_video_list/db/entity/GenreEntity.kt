package com.example.tv_video_list.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tv_video_list.db.Converters


@Entity(tableName = "genre_body")
@TypeConverters(Converters::class)
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String
)
