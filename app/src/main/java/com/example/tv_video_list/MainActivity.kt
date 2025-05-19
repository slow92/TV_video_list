package com.example.tv_video_list

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import com.example.tv_video_list.server.Movie
import com.example.tv_video_list.ui.theme.TV_Video_listTheme
import com.example.tv_video_list.viewmodel.MovieViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Icon


class MainActivity : ComponentActivity() {

    data class MoviesCategory(
        val title: String,
        val size: Int,
        val moviesList: List<Movie>
    )

    private val viewModel: MovieViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        viewModel.errorLivData.observe(this) { msg ->
            Toast.makeText(this, getString(msg), Toast.LENGTH_LONG).show()
            viewModel.errorLivData.value = 0
        }

        setContent {
            TV_Video_listTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    MovieScreen()
                }
            }
        }
    }

    @Composable
    fun MovieScreen() {
        val moviesPopular by viewModel.moviesPopular.collectAsState()
        val moviesTop by viewModel.moviesTop.collectAsState()
        val genresList by viewModel.genres.collectAsState()
        val configDetails by viewModel.configDetails.collectAsState()
        var imageUrlPrefix: String? = configDetails?.secure_base_url +
                configDetails?.backdrop_sizes?.last()

        val moviesData = listOf(
            MoviesCategory(getString(R.string.most_popular), 240, moviesPopular),
            MoviesCategory(getString(R.string.top_rated), 200, moviesTop),
            MoviesCategory(getString(R.string.most_popular), 500, moviesPopular)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(moviesData) { category ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    text = category.title,
                    color = Color.Gray,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleSmall
                )

                imageUrlPrefix?.let {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(category.moviesList) { movie ->
                            val genreString = genresList
                                .filter { it.id in movie.genre_ids }.joinToString(", ") { it.name }
                            MovieCard(
                                movie = movie,
                                size = category.size,
                                genreString = genreString,
                                imageUrlPrefix = imageUrlPrefix
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTvMaterial3Api::class)
    @Composable
    fun MovieCard(movie: Movie, size: Int, genreString: String, imageUrlPrefix: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .height((size.div(1.78) + 50).dp)
                .width(size.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .clickable(onClick = {}),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageUrlPrefix + movie.backdrop_path,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Icon(
                    Icons.Outlined.PlayArrow,
                    tint = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    contentDescription = getString(R.string.play_movie_icon)
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                text = movie.title,
                color = Color.White,
                fontSize = 12.sp,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                text = genreString,
                color = Color.LightGray,
                fontSize = 10.sp,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}