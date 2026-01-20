package ru.aza1rat.playlistmaker.search.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.aza1rat.playlistmaker.search.data.dto.TrackResponse

interface SearchAPI {
    @GET("search?entity=song")
    suspend fun getSongs(@Query("term") request: String) : TrackResponse
}