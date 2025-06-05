package com.example.tv_video_list.db.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tv_video_list.db.Converters

@Entity(tableName = "movie_body")
@TypeConverters(Converters::class)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val backdrop_path: String,
    val genre_ids: List<Int>
)
