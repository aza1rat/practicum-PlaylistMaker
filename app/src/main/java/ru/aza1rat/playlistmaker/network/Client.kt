package ru.aza1rat.playlistmaker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    
    private val retrofitClient = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val searchService: SearchAPI = retrofitClient.create(SearchAPI::class.java)


}