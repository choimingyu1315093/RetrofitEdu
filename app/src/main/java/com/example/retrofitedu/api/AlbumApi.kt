package com.example.retrofitedu.api

import com.example.retrofitedu.model.Album
import retrofit2.Call
import retrofit2.http.GET

interface AlbumApi {
    @GET("albums")
    fun getAlbums(): Call<List<Album>>
}