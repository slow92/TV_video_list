package com.example.tv_video_list.db
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.tv_video_list.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface TmdbDao {
    @Insert
    fun insertMovie(movieEntity: MovieEntity)

    @Insert
    fun insertMovies(movies: List<MovieEntity>)

    @Update
    fun updateMovie(movieEntity: MovieEntity)

    @Update
    fun updateMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_body WHERE id = :id")
    fun getMovieById(id: Int): LiveData<MovieEntity?>

    @Query("DELETE FROM movie_body")
    fun deleteAll()

    @Query("SELECT * FROM movie_body")
    fun getAllMovies(): Flow<List<MovieEntity>>


}