package ru.aza1rat.playlistmaker.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {
    @GET("search?entity=song")
    fun getSongs(@Query("term") request: String) : Call<TrackResponse>
}