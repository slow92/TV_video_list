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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Icon


class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        viewModel.fetchMovies("apiKey")
        viewModel.errorLivData.observe(this) { msg ->
            Toast.makeText(this, getString(msg), Toast.LENGTH_LONG).show()
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

    enum class Size { Large, Small }

    @Composable
    fun MovieScreen() {
        val moviesPopular by viewModel.moviesPopular.collectAsState()
        val moviesTop by viewModel.moviesTop.collectAsState()
        val genresList by viewModel.genres.collectAsState()
        val configDetails by viewModel.configDetails.collectAsState()
        val imageUrlPrefix = configDetails?.secure_base_url +
                configDetails?.backdrop_sizes?.last()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = getString(R.string.most_popular),
                color = Color.Gray,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleSmall
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(moviesPopular) { movie ->
                    val genreString = genresList
                        .filter { it.id in movie.genre_ids }.joinToString(", ") { it.name }
                    MovieCard(
                        movie = movie,
                        size = Size.Large,
                        genreString = genreString,
                        imageUrlPrefix = imageUrlPrefix
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = getString(R.string.top_rated),
                color = Color.Gray,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleSmall
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(moviesTop) { movie ->
                    val genreString = genresList
                        .filter { it.id in movie.genre_ids }.joinToString(", ") { it.name }
                    MovieCard(
                        movie = movie,
                        size = Size.Small,
                        genreString = genreString,
                        imageUrlPrefix = imageUrlPrefix
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalTvMaterial3Api::class)
    @Composable
    fun MovieCard(movie: Movie, size: Size, genreString: String, imageUrlPrefix: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .height(if (size == Size.Large) 160.dp else 120.dp)
                .width(if (size == Size.Large) 200.dp else 150.dp)
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
                    Icons.Default.PlayArrow,
                    tint = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(if (size == Size.Large) 8.dp else 5.dp),
                    contentDescription = ""
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                text = movie.title,
                color = Color.White,
                fontSize = if (size == Size.Large) 15.sp else 10.sp,
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
                fontSize = if (size == Size.Large) 10.sp else 7.sp,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}