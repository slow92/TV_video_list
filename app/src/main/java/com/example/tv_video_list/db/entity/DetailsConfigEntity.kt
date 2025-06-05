package com.example.tv_video_list.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tv_video_list.db.Converters

@Entity(tableName = "details_config_body")
@TypeConverters(Converters::class)
data class DetailsConfigEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val secure_base_url: String,
    val backdrop_sizes: List<String>
)
