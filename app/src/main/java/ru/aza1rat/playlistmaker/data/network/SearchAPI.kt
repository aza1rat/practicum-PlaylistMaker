package ru.aza1rat.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.aza1rat.playlistmaker.data.dto.TrackResponse

interface SearchAPI {
    @GET("search?entity=song")
    fun getSongs(@Query("term") request: String) : Call<TrackResponse>
}