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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
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

class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        viewModel.fetchMovies("TWO_API_KEY_HERE")
        viewModel.errorLivData.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
        setContent {
            TV_Video_listTheme {
                Greeting("Android")
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    enum class Size { Large, Small }

    @Composable
    fun MovieScreen() {
        val moviesPopular by viewModel.moviesPopular.collectAsState()
        val moviesTop by viewModel.moviesTop.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyRow {
                    items(moviesPopular) { movie ->
                        MovieCard(movie = movie, size = Size.Large)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow {
                    items(moviesTop) { movie ->
                        MovieCard(movie = movie, size = Size.Small)
                    }
                }
            }
        }
    }

    @Composable
    fun MovieCard(movie: Movie, size: Size) {
        Box(
            modifier = Modifier
                .size(if (size == Size.Large) 200.dp else 150.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .clickable {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                Text(text = movie.overview, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TV_Video_listTheme {
        Greeting("Android")
    }
}