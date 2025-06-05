package com.example.tv_video_list.db
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.tv_video_list.db.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VideoTvDatabase : RoomDatabase() {
    abstract fun tmdbDao(): TmdbDao

    companion object {
        @Volatile
        private var INSTANCE: VideoTvDatabase? = null

        fun getDatabase(context: Context): VideoTvDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoTvDatabase::class.java,
                    "movies_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
