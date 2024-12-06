package com.example.retrofitedu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.retrofitedu.ui.theme.RetrofitEduTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.retrofitedu.model.Album
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitEduTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AlbumScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumScreen() {
    var albums by remember { mutableStateOf<List<Album>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val call = RetrofitInstance.api.getAlbums()
        call.enqueue(object : Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                if (response.isSuccessful) {
                    albums = response.body()
                    Log.d(TAG, "onResponse: albums $albums")
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                errorMessage = "Failed to load data: ${t.message}"
                isLoading = false
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text(text = errorMessage ?: "Unknown Error")
            albums != null -> AlbumList(albums!!)
        }
    }
}

@Composable
fun AlbumList(albums: List<Album>) {
    LazyColumn {
        items(albums) { album ->
            AlbumItem(album)
        }
    }
}

@Composable
fun AlbumItem(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Album ID: ${album.id}", style = MaterialTheme.typography.bodyMedium)
            Text("Title: ${album.title}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}