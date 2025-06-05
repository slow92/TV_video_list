package com.example.tv_video_list

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.tv_video_list.server.Movie
import com.example.tv_video_list.ui.theme.TV_Video_listTheme
import com.example.tv_video_list.viewmodel.MovieViewModel
import com.example.tv_video_list.ui.screen.MovieScreen


class MainActivity : ComponentActivity() {

    data class MoviesCategory(
        val title: String,
        val size: Int,
        val moviesList: List<Movie>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        viewModel.errorLivData.observe(this) { msg ->
            Toast.makeText(this, getString(msg), Toast.LENGTH_LONG).show()
            viewModel.errorLivData.value = 0
        }

        setContent {
            TV_Video_listTheme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .padding(bottom = 20.dp)
                        .fillMaxSize()) {
                        MovieScreen(viewModel)
                    }
                }
            }
        }
    }
}